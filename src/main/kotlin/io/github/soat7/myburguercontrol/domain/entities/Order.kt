package io.github.soat7.myburguercontrol.domain.entities

import io.github.soat7.myburguercontrol.domain.entities.enum.OrderStatus
import io.github.soat7.myburguercontrol.util.DEFAULT_BIG_DECIMAL_SCALE
import io.github.soat7.myburguercontrol.util.toBigDecimal
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

data class Order(
    val id: UUID,
    val customerId: UUID? = null,
    val items: List<OrderItem> = listOf(),
    val status: OrderStatus = OrderStatus.RECEIVED,
    val createdAt: Instant = Instant.now(),
    val paymentId: UUID? = null,
) {
    val total: BigDecimal
        get() = items.sumOf { it.price * it.quantity.toBigDecimal() }.setScale(DEFAULT_BIG_DECIMAL_SCALE)
}
