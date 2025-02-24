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
    private val paymentUseCase: PaymentUseCase,
) {

    fun createOrder(orderDetail: OrderDetail): Order {
        return orderGateway.create(
            Order(
                id = UUID.randomUUID(),
                customerId = orderDetail.customerId,
                items = buildOrderItems(orderDetail),
                status = OrderStatus.RECEIVED,
            ),
        )
    }

    fun findOrdersByCustomerId(customerId: UUID): List<Order> {
        logger.info { "Order.findOrders(customerId = $customerId)" }

        return orderGateway.findByCustomerId(customerId)
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

        val paymentId = UUID.randomUUID()

        paymentUseCase.sendPaymentRequest(order)

        return orderGateway.update(
            order.copy(
                paymentId = paymentId,
                status = OrderStatus.PENDING_PAYMENT,
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
