package io.github.soat7.myburguercontrol.domain.usecase

import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.soat7.myburguercontrol.adapters.gateway.PaymentIntegrationRepository
import io.github.soat7.myburguercontrol.domain.entities.Order

private val logger = KotlinLogging.logger {}

class PaymentUseCase(
    private val paymentIntegrationRepository: PaymentIntegrationRepository,
) {

    fun sendPaymentRequest(order: Order) {
        paymentIntegrationRepository.requestPayment(order)
    }
}
