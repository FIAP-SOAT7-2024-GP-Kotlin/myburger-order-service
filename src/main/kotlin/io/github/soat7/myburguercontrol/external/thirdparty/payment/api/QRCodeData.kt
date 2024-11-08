package io.github.soat7.myburguercontrol.external.thirdparty.payment.api

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class QRCodeData(
    val qrData: String,
    val inStoreOrderId: String,
)
