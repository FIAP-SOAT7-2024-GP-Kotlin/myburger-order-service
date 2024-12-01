package io.github.soat7.myburguercontrol.external.webservice.order.api

import java.util.UUID

class OrderItemResponse(
    val productId: UUID,
    val quantity: Int,
    val comment: String? = null,
)
