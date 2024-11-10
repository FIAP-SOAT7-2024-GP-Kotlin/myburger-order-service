package io.github.soat7.myburguercontrol.base

import io.github.soat7.myburguercontrol.Application
import io.github.soat7.myburguercontrol.adapters.mapper.toPersistence
import io.github.soat7.myburguercontrol.container.MockServerContainer
import io.github.soat7.myburguercontrol.container.PostgresContainer
import io.github.soat7.myburguercontrol.domain.entities.Customer
import io.github.soat7.myburguercontrol.domain.entities.Payment
import io.github.soat7.myburguercontrol.domain.entities.enum.UserRole
import io.github.soat7.myburguercontrol.external.db.customer.entity.CustomerEntity
import io.github.soat7.myburguercontrol.external.db.customer.repository.CustomerJpaRepository
import io.github.soat7.myburguercontrol.external.db.order.model.OrderEntity
import io.github.soat7.myburguercontrol.external.db.order.repository.OrderDatabaseRepository
import io.github.soat7.myburguercontrol.external.db.payment.entity.PaymentEntity
import io.github.soat7.myburguercontrol.external.db.payment.repository.PaymentJpaRepository
import io.github.soat7.myburguercontrol.external.db.product.entity.ProductEntity
import io.github.soat7.myburguercontrol.external.db.product.repository.ProductJpaRepository
import io.github.soat7.myburguercontrol.external.db.user.repository.UserJpaRepository
import io.github.soat7.myburguercontrol.external.webservice.auth.api.AuthenticationResponse
import io.github.soat7.myburguercontrol.fixtures.AuthFixtures
import io.github.soat7.myburguercontrol.fixtures.OrderFixtures
import io.github.soat7.myburguercontrol.fixtures.ProductFixtures
import io.github.soat7.myburguercontrol.fixtures.UserFixtures
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.http.MediaType
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.ActiveProfiles
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import java.util.UUID

@ActiveProfiles("test")
@SpringBootTest(
    classes = [Application::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
)
@ExtendWith(PostgresContainer::class, MockServerContainer::class)
class BaseIntegrationTest {

    @Autowired
    protected lateinit var restTemplate: TestRestTemplate

    @Autowired
    protected lateinit var productJpaRepository: ProductJpaRepository

    @Autowired
    protected lateinit var customerJpaRepository: CustomerJpaRepository

    @Autowired
    protected lateinit var orderDatabaseRepository: OrderDatabaseRepository

    @Autowired
    protected lateinit var userJpaRepository: UserJpaRepository

    @Autowired
    protected lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    protected lateinit var paymentJpaRepository: PaymentJpaRepository

    protected lateinit var authenticationHeader: MultiValueMap<String, String>

    @BeforeEach
    fun setUpAuthentication() {
        println("Cleaning User database...")
        userJpaRepository.deleteAll()

        println("Cleaning Order database...")
        orderDatabaseRepository.deleteAll()

        authenticationHeader = buildAuthentication()
    }

    protected fun insertProducts(): List<ProductEntity> {
        productJpaRepository.save(ProductFixtures.mockProductEntity())
        productJpaRepository.save(ProductFixtures.mockProductEntity())

        return productJpaRepository.findAll()
    }

    protected fun insertCustomerData(customer: Customer): CustomerEntity {
        return customerJpaRepository.save(customer.toPersistence())
    }

    protected fun insertPaymentData(payment: Payment) = paymentJpaRepository.save(payment.toPersistence())

    protected fun buildAuthentication(): MultiValueMap<String, String> {
        val cpf = "15666127055"
        val password = UUID.randomUUID().toString()
        val userRole = UserRole.ADMIN
        userJpaRepository.save(
            UserFixtures.mockUserEntity(
                cpf = cpf,
                password = passwordEncoder.encode(password),
                userRole = userRole,
            ),
        )

        val response = restTemplate.postForEntity<AuthenticationResponse>(
            "/auth",
            AuthFixtures.mockAuthCreationRequest(cpf, password),
        ).body
            ?: throw RuntimeException("Failed to authenticate")

        val header: MultiValueMap<String, String> = LinkedMultiValueMap()
        header.add("Authorization", "Bearer ${response.accessToken}")
        header.add("Content-Type", MediaType.APPLICATION_JSON_VALUE)

        return header
    }

    protected fun saveOrder(
        customer: CustomerEntity,
        product: ProductEntity,
        payment: PaymentEntity,
        status: String? = null,
    ): OrderEntity {
        return orderDatabaseRepository.create(OrderFixtures.mockOrderEntity(customer, product, payment, status))
    }
}
