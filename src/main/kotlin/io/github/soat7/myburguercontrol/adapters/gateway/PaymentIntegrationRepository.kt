package io.github.soat7.myburguercontrol.adapters.gateway

import io.github.soat7.myburguercontrol.domain.entities.Order
import io.github.soat7.myburguercontrol.external.thirdparty.payment.api.QRCodeData
import java.util.UUID

interface PaymentIntegrationRepository {

    fun requestPayment(order: Order, paymentId: UUID): QRCodeData
}
