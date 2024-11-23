package io.github.soat7.myburguercontrol.domain.usecase

import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.soat7.myburguercontrol.domain.entities.Order
import io.github.soat7.myburguercontrol.domain.entities.OrderDetail
import io.github.soat7.myburguercontrol.domain.entities.OrderItem
import io.github.soat7.myburguercontrol.domain.entities.enum.OrderStatus
import io.github.soat7.myburguercontrol.exception.ReasonCode
import io.github.soat7.myburguercontrol.exception.ReasonCodeException
import io.github.soat7.myburguercontrol.external.db.order.OrderGateway
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

private val logger = KotlinLogging.logger {}

class OrderUseCase(
    private val orderGateway: OrderGateway,
    private val customerUseCase: CustomerUseCase,
    private val paymentUseCase: PaymentUseCase,
) {

    fun createOrder(orderDetail: OrderDetail): Order {
        val customer = orderDetail.customerCpf
            ?.takeIf { it.isNotBlank() }
            ?.let {
                customerUseCase.findCustomerByCpf(it)
                    ?: throw ReasonCodeException(ReasonCode.CUSTOMER_NOT_FOUND)
            }

        return orderGateway.create(
            Order(
                id = UUID.randomUUID(),
                customerId = customer?.id,
                items = buildOrderItems(orderDetail),
                status = OrderStatus.PENDING_PAYMENT,
            ),
        )
    }

    fun findOrdersByCustomerCpf(cpf: String): List<Order> {
        logger.info { "Order.findOrders(cpf = $cpf)" }
        val customer = customerUseCase.findCustomerByCpf(cpf)
            ?: throw ReasonCodeException(ReasonCode.CUSTOMER_NOT_FOUND)

        return orderGateway.findByCustomerId(customer.id)
    }

    fun findQueuedOrders(pageable: Pageable): Page<Order> {
        logger.info { "Finding orders with status: [${OrderStatus.RECEIVED}]" }
        return orderGateway.findNewOrders(OrderStatus.RECEIVED.name, pageable)
    }

    fun findAll(pageable: Pageable): Page<Order> {
        logger.info { "Listing orders" }
        return orderGateway.findAll(pageable)
    }

    fun changeOrderStatus(status: OrderStatus, orderId: UUID): Order {
        logger.info { "Changing order status to: [$status] for order: [$orderId]" }
        return orderGateway.findById(orderId)
            ?.let { orderGateway.update(it.copy(status = status)) }
            ?: throw ReasonCodeException(ReasonCode.ORDER_NOT_FOUND)
    }

    fun sendOrderPayment(orderId: UUID): Order {
        logger.info { "Sending order payment" }

        val order = orderGateway.findById(orderId)
            ?: throw ReasonCodeException(ReasonCode.ORDER_NOT_FOUND)

        val payment = paymentUseCase.sendPaymentRequest(order)

        return orderGateway.update(
            order.copy(
                paymentId = payment.id,
                status = OrderStatus.RECEIVED,
            ),
        )
    }

    private fun buildOrderItems(orderDetail: OrderDetail): List<OrderItem> {
        return orderDetail.items.map { item ->
            OrderItem(
                productId = item.productId,
                quantity = item.quantity,
                comment = item.comment,
                price = item.price,
            )
        }
    }
}
