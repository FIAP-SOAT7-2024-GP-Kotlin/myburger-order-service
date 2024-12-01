package io.github.soat7.myburguercontrol.domain.entities

import java.math.BigDecimal
import java.util.UUID

data class OrderDetail(
    val customerId: UUID,
    val items: List<OrderItemDetail>,
    val comment: String? = null,
) {
    data class OrderItemDetail(
        val productId: UUID,
        val price: BigDecimal,
        val quantity: Int,
        val comment: String? = null,
    )
}
