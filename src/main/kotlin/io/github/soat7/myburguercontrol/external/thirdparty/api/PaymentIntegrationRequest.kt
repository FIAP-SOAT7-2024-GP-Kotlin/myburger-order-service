package io.github.soat7.myburguercontrol.external.thirdparty.api

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.math.BigDecimal

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class PaymentIntegrationRequest(
    val description: String,
    val externalReference: String,
    val items: List<Item>,
    val notificationUrl: String,
    val totalAmount: BigDecimal,
    val title: String = "Order",
)

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class Item(
    val title: String,
    val description: String,
    val unitPrice: BigDecimal,
    val quantity: Int,
    val unitMeasure: String,
    val totalAmount: BigDecimal,
)
