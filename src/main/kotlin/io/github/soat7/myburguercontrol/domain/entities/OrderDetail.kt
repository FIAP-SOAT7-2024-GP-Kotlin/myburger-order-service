package io.github.soat7.myburguercontrol.domain.entities

import java.math.BigDecimal
import java.util.UUID

data class OrderDetail(
    val customerCpf: String? = null,
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
