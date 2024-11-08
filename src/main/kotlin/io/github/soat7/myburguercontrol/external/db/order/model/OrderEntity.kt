package io.github.soat7.myburguercontrol.external.db.order.model

import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant
import java.util.UUID

@Document("orders")
class OrderEntity(
    val id: UUID,
    val customerId: UUID? = null,
    val status: String,
    val items: List<OrderItemEntity> = mutableListOf(),
    val paymentId: UUID?,
    val createdAt: Instant,
    val updatedAt: Instant,
)
