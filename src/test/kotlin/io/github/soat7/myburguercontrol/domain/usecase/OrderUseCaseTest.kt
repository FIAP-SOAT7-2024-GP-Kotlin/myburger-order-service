package io.github.soat7.myburguercontrol.domain.usecase

import io.github.soat7.myburguercontrol.domain.entities.enum.OrderStatus
import io.github.soat7.myburguercontrol.exception.ReasonCodeException
import io.github.soat7.myburguercontrol.external.db.order.OrderGateway
import io.github.soat7.myburguercontrol.fixtures.OrderDetailFixtures
import io.github.soat7.myburguercontrol.fixtures.OrderFixtures
import io.github.soat7.myburguercontrol.util.toBigDecimal
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestMethodOrder
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.function.Executable
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
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

    private val gateway = mockk<OrderGateway>()
    private val paymentUseCase = mockk<PaymentUseCase>()
    private val service = OrderUseCase(gateway, paymentUseCase)

    @BeforeTest
    fun setUp() {
        clearAllMocks()
    }

    @Test
    @Order(1)
    fun `should create a new order using cpf`() {
        val customerId = UUID.randomUUID()
        every { gateway.create(any<OrderModel>()) } answers {
            (this.firstArg() as OrderModel).copy(id = UUID.randomUUID())
        }
        every { gateway.update(any<OrderModel>()) } answers {
            (this.firstArg() as OrderModel).copy(id = UUID.randomUUID())
        }

        val order = service.createOrder(OrderDetailFixtures.mockOrderDetail(customerId = customerId))

        verify(exactly = 1) { gateway.create(any()) }

        Assertions.assertAll(
            Executable { assertNotNull(order.id) },
            Executable { assertEquals(OrderStatus.RECEIVED, order.status) },
            Executable { assertFalse(order.items.isEmpty()) },
            Executable { assertEquals(5.99.toBigDecimal(), order.total) },
        )
    }

    @Test
    fun `Should successfully change order status`() {
        val customerId = UUID.randomUUID()
        val order = OrderFixtures.mockOrder(customerId = customerId)

        every { gateway.findById(any()) } returns order
        every { gateway.update(any()) } returns order.copy(status = OrderStatus.IN_PROGRESS)

        assertDoesNotThrow { service.changeOrderStatus(OrderStatus.IN_PROGRESS, orderId = order.id) }

        verify(exactly = 1) { gateway.findById(any()) }
        verify(exactly = 1) { gateway.update(any()) }
    }

    @Test
    fun `Should throw ReasonCodeException when no Order is found when trying to change order status`() {
        every { gateway.findById(any()) } returns null

        Assertions.assertThrows(ReasonCodeException::class.java) {
            service.changeOrderStatus(OrderStatus.IN_PROGRESS, orderId = UUID.randomUUID())
        }

        verify(exactly = 1) { gateway.findById(any()) }
        verify(exactly = 0) { gateway.update(any()) }
    }

    @Test
    fun `should find queued orders`() {
        val pageable = PageRequest.of(0, 10)
        val orders = listOf(
            OrderModel(
                id = UUID.randomUUID(),
                customerId = UUID.randomUUID(),
                items = emptyList(),
                status = OrderStatus.RECEIVED,
            ),
        )
        val page: Page<OrderModel> = PageImpl(orders, pageable, orders.size.toLong())

        every { gateway.findNewOrders(any(), any()) } returns page

        val result = service.findQueuedOrders(pageable)

        verify(exactly = 1) { gateway.findNewOrders(any(), any()) }
        assertEquals(page, result)
    }

    @Test
    fun `should return empty page when no queued orders found`() {
        val pageable = PageRequest.of(0, 10)
        val page: Page<OrderModel> = PageImpl(emptyList(), pageable, 0)

        every { gateway.findNewOrders(any(), any()) } returns page

        val result = service.findQueuedOrders(pageable)

        verify(exactly = 1) { gateway.findNewOrders(any(), any()) }
        assertEquals(page, result)
    }

    @Test
    fun `should successfully update an order after sending order payment`() {
        val customerId = UUID.randomUUID()
        val order = OrderFixtures.mockOrder(customerId)
        val paymentId = UUID.randomUUID()

        every { gateway.findById(any()) } returns order
        every { paymentUseCase.sendPaymentRequest(any(), any()) } just runs
        every { gateway.update(any()) } returns order.copy(paymentId = paymentId, status = OrderStatus.PENDING_PAYMENT)

        assertDoesNotThrow { service.sendOrderPayment(order.id) }

        verify(exactly = 1) { gateway.findById(any()) }
        verify(exactly = 1) { paymentUseCase.sendPaymentRequest(any(), any()) }
        verify(exactly = 1) { gateway.update(any()) }
    }

    @Test
    fun `should throw ReasonCodeException when no Order is found when trying to send order payment`() {
        val orderId = UUID.randomUUID()
        every { gateway.findById(any()) } returns null

        Assertions.assertThrows(ReasonCodeException::class.java) {
            service.sendOrderPayment(orderId)
        }

        verify(exactly = 1) { gateway.findById(any()) }
        verify(exactly = 0) { paymentUseCase.sendPaymentRequest(any(), any()) }
        verify(exactly = 0) { gateway.update(any()) }
    }
}
