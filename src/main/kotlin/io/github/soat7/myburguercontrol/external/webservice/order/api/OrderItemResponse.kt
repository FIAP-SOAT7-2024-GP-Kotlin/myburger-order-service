package io.github.soat7.myburguercontrol.external.webservice.order.api

import java.math.BigDecimal
import java.util.UUID

class OrderItemResponse(
    val productId: UUID,
    val quantity: Int,
    val comment: String? = null,
) {
    data class OrderItemProductResponse(
        val productId: UUID,
        val name: String,
        val price: BigDecimal,
    )
}
