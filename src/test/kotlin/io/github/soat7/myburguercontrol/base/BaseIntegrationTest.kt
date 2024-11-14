package io.github.soat7.myburguercontrol.base

import io.github.soat7.myburguercontrol.Application
import io.github.soat7.myburguercontrol.adapters.mapper.toPersistence
import io.github.soat7.myburguercontrol.container.MockServerContainer
import io.github.soat7.myburguercontrol.container.MongoDbContainer
import io.github.soat7.myburguercontrol.container.PostgresContainer
import io.github.soat7.myburguercontrol.domain.entities.Customer
import io.github.soat7.myburguercontrol.domain.entities.Payment
import io.github.soat7.myburguercontrol.external.db.customer.model.CustomerEntity
import io.github.soat7.myburguercontrol.external.db.customer.repository.CustomerDatabaseRepository
import io.github.soat7.myburguercontrol.external.db.order.model.OrderEntity
import io.github.soat7.myburguercontrol.external.db.order.repository.OrderDatabaseRepository
import io.github.soat7.myburguercontrol.external.db.payment.repository.PaymentDatabaseRepository
import io.github.soat7.myburguercontrol.fixtures.OrderFixtures
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.test.context.ActiveProfiles
import java.util.UUID

@ActiveProfiles("test")
@SpringBootTest(
    classes = [Application::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
)
@ExtendWith(PostgresContainer::class, MockServerContainer::class, MongoDbContainer::class)
class BaseIntegrationTest {

    @Value("\${spring.data.mongodb.collection.orders}")
    private lateinit var ordersCollection: String

    @Value("\${spring.data.mongodb.collection.customers}")
    private lateinit var customersCollection: String

    @Autowired
    protected lateinit var restTemplate: TestRestTemplate

    @Autowired
    protected lateinit var customerDatabaseRepository: CustomerDatabaseRepository

    @Autowired
    protected lateinit var orderDatabaseRepository: OrderDatabaseRepository

    @Autowired
    protected lateinit var paymentDatabaseRepository: PaymentDatabaseRepository

    @Autowired
    private lateinit var mongoDbTemplate: MongoTemplate

    @BeforeEach
    fun cleanUpd() {
        println("Cleaning up database.....")
        mongoDbTemplate.getCollection(ordersCollection).deleteMany(org.bson.Document())
        mongoDbTemplate.getCollection(customersCollection).deleteMany(org.bson.Document())
    }

    protected fun insertCustomerData(customer: Customer): CustomerEntity {
        return customerDatabaseRepository.create(customer.toPersistence())
    }

    protected fun insertPaymentData(payment: Payment) = paymentDatabaseRepository.create(payment.toPersistence())

    protected fun saveOrder(
        customerId: UUID,
        productId: UUID = UUID.randomUUID(),
        paymentId: UUID,
        status: String? = null,
    ): OrderEntity {
        return orderDatabaseRepository.create(OrderFixtures.mockOrderEntity(customerId, productId, paymentId, status))
    }
}
