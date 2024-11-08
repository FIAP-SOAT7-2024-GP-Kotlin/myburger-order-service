package io.github.soat7.myburguercontrol.domain.entities

import java.math.BigDecimal
import java.util.UUID

data class OrderItem(
    val productId: UUID,
    val price: BigDecimal,
    val quantity: Int,
    val comment: String? = null,
)
