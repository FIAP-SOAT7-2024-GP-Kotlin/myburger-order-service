package io.github.soat7.myburguercontrol.external.db.order.model

import java.math.BigDecimal
import java.util.UUID

class OrderItemEntity(
    val productId: UUID,
    val price: BigDecimal,
    val quantity: Int,
    val comment: String? = null,
)
