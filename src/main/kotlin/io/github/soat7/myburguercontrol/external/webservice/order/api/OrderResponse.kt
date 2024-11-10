package io.github.soat7.myburguercontrol.external.webservice.order.api

import io.github.soat7.myburguercontrol.domain.entities.enum.OrderStatus
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

data class OrderResponse(
    val id: UUID,
    val customerId: UUID?,
    val items: MutableList<OrderItemResponse> = mutableListOf(),
    val status: OrderStatus,
    val createdAt: Instant,
    val total: BigDecimal,
)
