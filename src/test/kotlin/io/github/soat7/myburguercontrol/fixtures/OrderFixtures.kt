package io.github.soat7.myburguercontrol.fixtures

import io.github.soat7.myburguercontrol.domain.entities.Order
import io.github.soat7.myburguercontrol.domain.entities.OrderItem
import io.github.soat7.myburguercontrol.domain.entities.enum.OrderStatus
import io.github.soat7.myburguercontrol.external.db.order.model.OrderEntity
import io.github.soat7.myburguercontrol.external.db.order.model.OrderItemEntity
import java.time.Instant
import java.util.UUID

object OrderFixtures {

    fun mockOrder(
        customerId: UUID,
        paymentId: UUID = UUID.randomUUID(),
        productId: UUID = UUID.randomUUID(),
    ) = Order(
        id = UUID.randomUUID(),
        customerId = customerId,
        paymentId = paymentId,
        items = listOf(
            OrderItem(
                productId = productId,
                quantity = 1,
                price = 5.99.toBigDecimal(),
            ),
        ),
    )

    fun mockOrderEntity(
        customerId: UUID,
        productId: UUID,
        paymentId: UUID,
        status: String? = null,
    ) = OrderEntity(
        id = UUID.randomUUID(),
        customerId = customerId,
        status = status ?: OrderStatus.RECEIVED.name,
        createdAt = Instant.now(),
        paymentId = paymentId,
        items = listOf(
            OrderItemEntity(
                productId = productId,
                quantity = 1,
                price = 5.99.toBigDecimal(),
            ),
        ),
        updatedAt = Instant.now(),
    )
}
