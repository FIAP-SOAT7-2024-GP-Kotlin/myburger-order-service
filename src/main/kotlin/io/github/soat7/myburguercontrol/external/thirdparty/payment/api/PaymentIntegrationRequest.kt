package io.github.soat7.myburguercontrol.external.thirdparty.payment.api

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.math.BigDecimal

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class PaymentIntegrationRequest(
    val orderItems: List<Item>,
    val orderPrice: BigDecimal,
    val title: String = "Order",
    val orderId: String,
)

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class Item(
    val unitPrice: BigDecimal,
    val quantity: Int,
    val unitMeasure: String,
    val totalAmount: BigDecimal,
)
