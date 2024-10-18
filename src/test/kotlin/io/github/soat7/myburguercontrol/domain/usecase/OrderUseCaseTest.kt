package io.github.soat7.myburguercontrol.domain.usecase

import io.github.soat7.myburguercontrol.domain.entities.enum.OrderStatus
import io.github.soat7.myburguercontrol.external.db.order.OrderGateway
import io.github.soat7.myburguercontrol.fixtures.CustomerFixtures.mockDomainCustomer
import io.github.soat7.myburguercontrol.fixtures.OrderDetailFixtures
import io.github.soat7.myburguercontrol.fixtures.ProductFixtures
import io.github.soat7.myburguercontrol.util.toBigDecimal
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestMethodOrder
import java.util.UUID
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import io.github.soat7.myburguercontrol.domain.entities.Order as OrderModel

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class OrderUseCaseTest {

    private val repository = mockk<OrderGateway>()
    private val customerUseCase = mockk<CustomerUseCase>()
    private val productUseCase = mockk<ProductUseCase>()
    private val service = OrderUseCase(repository, customerUseCase, productUseCase)

    @BeforeTest
    fun setUp() {
        clearMocks(customerUseCase)
        clearMocks(repository)
    }

    @Test
    @Order(1)
    fun `should create a new order using cpf`() {
        val cpf = "23282711034"
        val customer = mockDomainCustomer(cpf = cpf)
        val product = ProductFixtures.mockDomainProduct()

        every { customerUseCase.findCustomerByCpf(cpf) } returns customer
        every { repository.create(any<OrderModel>()) } answers {
            (this.firstArg() as OrderModel).copy(id = UUID.randomUUID())
        }
        every { productUseCase.findById(any()) } returns product
        every { repository.update(any<OrderModel>()) } answers {
            (this.firstArg() as OrderModel).copy(id = UUID.randomUUID())
        }

        val order = service.createOrder(OrderDetailFixtures.mockOrderDetail(cpf = cpf, product = product))

        verify(exactly = 1) { customerUseCase.findCustomerByCpf(any()) }
        verify(exactly = 2) { repository.update(any()) }
        verify(exactly = 1) { repository.create(any()) }

        assertNotNull(order.id)
        order.customer?.let { assertEquals(cpf, it.cpf) }
        assertEquals(OrderStatus.RECEIVED, order.status)
        assertFalse(order.items.isEmpty())
        assertEquals(1.0.toBigDecimal(), order.total)
    }
}
