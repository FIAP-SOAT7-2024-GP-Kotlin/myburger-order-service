package io.github.soat7.myburguercontrol.webservice

import io.github.soat7.myburguercontrol.base.BaseIntegrationTest
import io.github.soat7.myburguercontrol.domain.entities.enum.OrderStatus
import io.github.soat7.myburguercontrol.external.webservice.common.PaginatedResponse
import io.github.soat7.myburguercontrol.external.webservice.order.api.OrderCreationRequest
import io.github.soat7.myburguercontrol.external.webservice.order.api.OrderResponse
import io.github.soat7.myburguercontrol.fixtures.CustomerFixtures.mockDomainCustomer
import io.github.soat7.myburguercontrol.fixtures.OrderFixtures
import io.github.soat7.myburguercontrol.fixtures.PaymentFixtures.mockPayment
import io.github.soat7.myburguercontrol.mock.PaymentServiceMock
import io.github.soat7.myburguercontrol.util.toBigDecimal
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import org.springframework.boot.test.web.client.exchange
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import java.math.BigDecimal
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertNull

class OrderIT : BaseIntegrationTest() {

    @Test
    fun `should create a new order`() {
        val cpf = "23282711034"
        val customer = insertCustomerData(mockDomainCustomer(cpf = cpf))
        val items = listOf(
            OrderCreationRequest.OrderItem(
                productId = UUID.randomUUID(),
                quantity = 1,
                price = 5.99.toBigDecimal(),
            ),
        )

        val inputOrderData = OrderCreationRequest(customerCpf = customer.cpf, items)

        val orderResponse = restTemplate.exchange<OrderResponse>(
            url = "/orders",
            method = HttpMethod.POST,
            requestEntity = HttpEntity(inputOrderData),
        )

        assertAll(
            Executable { assertTrue(orderResponse.statusCode.is2xxSuccessful) },
            Executable { assertNotNull(orderResponse.body) },
        )

        val order = orderDatabaseRepository.findById(orderResponse.body!!.id)

        assertAll(
            Executable { assertNotNull(order) },
            Executable { assertEquals(customer.id, order!!.customerId) },
            Executable { assertEquals(OrderStatus.PENDING_PAYMENT.name, order!!.status) },
            Executable { assertFalse(order!!.items.isEmpty()) },
        )
    }

    @Test
    fun `should successfully find all orders with status different of FINISHED and ordered by status and createdAt`() {
        val cpf = "34187595058"
        val customer = insertCustomerData(mockDomainCustomer(cpf = cpf))
        val payment = insertPaymentData(mockPayment())
        OrderFixtures.mockOrderEntity(
            customerId = customer.id,
            status = OrderStatus.RECEIVED.name,
        )

        val order = saveOrder(OrderFixtures.mockOrderEntity(customerId = customer.id, paymentId = payment.id))
        val inProgressOrder =
            saveOrder(
                OrderFixtures.mockOrderEntity(
                    customerId = customer.id,
                    paymentId = payment.id,
                    status = OrderStatus.IN_PROGRESS.name,
                ),
            )
        val readyOrder =
            saveOrder(
                OrderFixtures.mockOrderEntity(
                    customerId = customer.id,
                    paymentId = payment.id,
                    status = OrderStatus.READY.name,
                ),
            )

        val response = restTemplate.exchange<PaginatedResponse<OrderResponse>>(
            url = "/orders/list",
            method = HttpMethod.GET,
            requestEntity = null,
        )

        val orders = response.body!!.content

        assertAll(
            Executable { assertNotNull(response.body) },
            Executable { assertFalse(response.body!!.content.isEmpty()) },
            Executable { assertEquals(readyOrder.id, orders.first().id) },
            Executable { assertEquals(inProgressOrder.id, orders[1].id) },
            Executable { assertEquals(order.id, orders[2].id) },
        )
    }

    @Test
    fun `should get orders using cpf`() {
        val cpf = "47052551004"

        val customer = insertCustomerData(mockDomainCustomer(cpf = cpf))
        val payment = insertPaymentData(mockPayment())
        saveOrder(OrderFixtures.mockOrderEntity(customerId = customer.id, paymentId = payment.id))

        val orders = restTemplate.exchange<List<OrderResponse>>(
            url = "/orders?cpf={cpf}",
            method = HttpMethod.GET,
            requestEntity = null,
            uriVariables = mapOf(
                "cpf" to cpf,
            ),
        )

        assertAll(
            Executable { assertNotNull(orders.body) },
            Executable { assertFalse(orders.body!!.isEmpty()) },
        )

        val order = orders.body!!.first()

        assertAll(
            Executable { assertNotNull(order.id) },
            Executable { assertEquals(customer.id, order.customerId!!) },
            Executable { assertEquals(OrderStatus.RECEIVED, order.status) },
            Executable { assertFalse(order.items.isEmpty()) },
            Executable { assertEquals(5.99.toBigDecimal(), order.total) },
        )
    }

    @Test
    fun `should return BAD_REQUEST when trying to create an order for a customer that was not found`() {
        val cpf = "44527073001"
        val items =
            listOf(
                OrderCreationRequest.OrderItem(
                    productId = UUID.randomUUID(),
                    quantity = 1,
                    price = BigDecimal.ONE,
                ),
            )

        val inputOrderData = OrderCreationRequest(customerCpf = cpf, items)

        val response = restTemplate.exchange<Any>(
            url = "/orders",
            method = HttpMethod.POST,
            requestEntity = HttpEntity(inputOrderData),
        )

        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
    }

    @Test
    fun `should create a new order when cpf is not filled in`() {
        val cpf = ""
        val customer = insertCustomerData(mockDomainCustomer(cpf = cpf))
        val items =
            listOf(
                OrderCreationRequest.OrderItem(
                    productId = UUID.randomUUID(),
                    quantity = 1,
                    price = 5.99.toBigDecimal(),
                ),
            )

        val inputOrderData = OrderCreationRequest(customerCpf = customer.cpf, items)

        val orderResponse = restTemplate.exchange<OrderResponse>(
            url = "/orders",
            method = HttpMethod.POST,
            requestEntity = HttpEntity(inputOrderData),
        )

        assertAll(
            Executable { assertTrue(orderResponse.statusCode.is2xxSuccessful) },
            Executable { assertNotNull(orderResponse.body) },
        )

        val order = orderDatabaseRepository.findById(orderResponse.body!!.id)

        assertAll(
            Executable { assertNotNull(order) },
            Executable { assertNull(order!!.customerId) },
            Executable { assertEquals(OrderStatus.PENDING_PAYMENT.name, order!!.status) },
            Executable { assertFalse(order!!.items.isEmpty()) },
        )
    }

    @Test
    fun `should find queue orders`() {
        val cpf = "47052551004"
        val customer = insertCustomerData(mockDomainCustomer(cpf = cpf))
        val order = saveOrder(OrderFixtures.mockOrderEntity(customerId = customer.id))
        saveOrder(OrderFixtures.mockOrderEntity(customerId = customer.id))
        saveOrder(OrderFixtures.mockOrderEntity(customerId = customer.id))
        saveOrder(OrderFixtures.mockOrderEntity(customerId = customer.id))
        saveOrder(OrderFixtures.mockOrderEntity(customerId = customer.id))

        val response =
            restTemplate.exchange<PaginatedResponse<OrderResponse>>(
                url = "/orders/queue",
                method = HttpMethod.GET,
                requestEntity = null,
            )

        println(response)

        assertAll(
            Executable { assertNotNull(response.body) },
            Executable { assertFalse(response.body!!.content.isEmpty()) },
            Executable { assertEquals(order.id, response.body!!.content[0].id) },
        )
    }

    @Test
    fun `should successfully send a payment request`() {
        val cpf = "47052551004"
        val customer = insertCustomerData(mockDomainCustomer(cpf = cpf))
        val order = saveOrder(
            OrderFixtures.mockOrderEntity(
                customerId = customer.id,
                status = OrderStatus.PENDING_PAYMENT.name,
            ),
        )

        PaymentServiceMock.sendPaymentRequest()

        restTemplate.exchange<Any>(
            url = "/orders/payment",
            method = HttpMethod.POST,
            requestEntity = HttpEntity(order.id),
        )
            .statusCode
            .isSameCodeAs(HttpStatus.NO_CONTENT)
    }

    @Test
    fun `should return 404 when sending payment for an order that does not exist`() {
        val orderId = UUID.randomUUID()

        restTemplate.exchange<Any>(
            url = "/orders/payment",
            method = HttpMethod.POST,
            requestEntity = HttpEntity(orderId),
        )
            .statusCode
            .isSameCodeAs(HttpStatus.NOT_FOUND)
    }

    @Test
    fun `should successfully update order status to READY`() {
        val cpf = "47052551004"
        val customer = insertCustomerData(mockDomainCustomer(cpf = cpf))
        val order = saveOrder(
            OrderFixtures.mockOrderEntity(
                customerId = customer.id,
                status = OrderStatus.PENDING_PAYMENT.name,
            ),
        )

        restTemplate.exchange<Any>(
            url = "/orders/ready",
            method = HttpMethod.POST,
            requestEntity = HttpEntity(order.id),
        )
            .statusCode
            .isSameCodeAs(HttpStatus.OK)

        val storedOrder = orderDatabaseRepository.findById(order.id)

        assertNotNull(storedOrder)
        assertEquals(OrderStatus.READY.name, storedOrder!!.status)
    }

    @Test
    fun `should successfully update order status to FINISHED`() {
        val cpf = "47052551004"
        val customer = insertCustomerData(mockDomainCustomer(cpf = cpf))
        val order = saveOrder(
            OrderFixtures.mockOrderEntity(
                customerId = customer.id,
                status = OrderStatus.PENDING_PAYMENT.name,
            ),
        )

        restTemplate.exchange<Any>(
            url = "/orders/finished",
            method = HttpMethod.POST,
            requestEntity = HttpEntity(order.id),
        )
            .statusCode
            .isSameCodeAs(HttpStatus.OK)

        val storedOrder = orderDatabaseRepository.findById(order.id)

        assertNotNull(storedOrder)
        assertEquals(OrderStatus.FINISHED.name, storedOrder!!.status)
    }

    @Test
    fun `should successfully update order status to IN_PROGRESS`() {
        val cpf = "47052551004"
        val customer = insertCustomerData(mockDomainCustomer(cpf = cpf))
        val order = saveOrder(
            OrderFixtures.mockOrderEntity(
                customerId = customer.id,
                status = OrderStatus.PENDING_PAYMENT.name,
            ),
        )

        val response = restTemplate.exchange<Any>(
            url = "/orders/in-progress",
            method = HttpMethod.POST,
            requestEntity = HttpEntity(order.id),
        )
            .statusCode
            .isSameCodeAs(HttpStatus.OK)

        val storedOrder = orderDatabaseRepository.findById(order.id)

        assertNotNull(storedOrder)
        assertEquals(OrderStatus.IN_PROGRESS.name, storedOrder!!.status)
    }
}
