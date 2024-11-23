package io.github.soat7.myburguercontrol.domain.usecase

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.soat7.myburguercontrol.adapters.gateway.PaymentIntegrationRepository
import io.github.soat7.myburguercontrol.domain.entities.Order
import io.github.soat7.myburguercontrol.domain.entities.Payment
import io.github.soat7.myburguercontrol.external.db.order.OrderGateway
import io.github.soat7.myburguercontrol.external.db.payment.PaymentGateway

private val logger = KotlinLogging.logger {}

class PaymentUseCase(
    private val paymentIntegrationRepository: PaymentIntegrationRepository,
    private val paymentGateway: PaymentGateway,
    private val orderGateway: OrderGateway,
) {

    fun sendPaymentRequest(order: Order): Payment {
        val payment = createPayment()

        val qrCodeData = paymentIntegrationRepository.requestPayment(order, payment.id)

        return payment.copy(
            metadata = jacksonObjectMapper().convertValue(qrCodeData, Map::class.java) as Map<String, Any>,
        )
    }

    private fun createPayment(): Payment {
        logger.info { "Creating payment" }

        return paymentGateway.create(Payment())
    }
}
