package io.github.soat7.myburguercontrol.domain.usecase

import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.soat7.myburguercontrol.adapters.gateway.PaymentIntegrationRepository
import io.github.soat7.myburguercontrol.domain.entities.Payment
import io.github.soat7.myburguercontrol.domain.entities.enum.PaymentStatus
import io.github.soat7.myburguercontrol.exception.ReasonCode
import io.github.soat7.myburguercontrol.exception.ReasonCodeException
import io.github.soat7.myburguercontrol.external.db.order.OrderGateway
import io.github.soat7.myburguercontrol.external.db.payment.PaymentGateway
import io.github.soat7.myburguercontrol.external.thirdparty.api.QRCodeData
import java.time.Instant
import java.util.UUID

private val logger = KotlinLogging.logger {}

class PaymentUseCase(
    private val paymentIntegrationRepository: PaymentIntegrationRepository,
    private val paymentGateway: PaymentGateway,
    private val orderGateway: OrderGateway,
) {

    fun startPaymentRequest(orderId: UUID): QRCodeData {
        val order = orderGateway.findById(orderId) ?: throw ReasonCodeException(ReasonCode.ORDER_NOT_FOUND)

        val payment = createPayment()
        val orderUpdated = order.copy(
            payment = payment,
        )
        orderGateway.update(orderUpdated)

        return paymentIntegrationRepository.requestQRCodeDataForPayment(orderUpdated)
    }

    private fun createPayment(): Payment {
        logger.info { "Creating payment" }

        return paymentGateway.create(Payment())
    }

    fun updatePayment(paymentId: String, paymentStatus: String): Payment {
        logger.info { "Update payment: $paymentId status: $paymentStatus" }

        val payment = paymentId.let {
            paymentGateway.findById(UUID.fromString(paymentId))
        } ?: throw ReasonCodeException(ReasonCode.PAYMENT_NOT_FOUND)

        val updatedPayment = payment.copy(
            status = PaymentStatus.fromString(paymentStatus),
            updatedAt = Instant.now(),
        )
        paymentGateway.update(updatedPayment)

        logger.info { "Successfully update with status return: [${updatedPayment.status.name}]" }

        if (updatedPayment.status == PaymentStatus.DENIED) throw ReasonCodeException(ReasonCode.PAYMENT_INTEGRATION_ERROR)

        return updatedPayment
    }

    fun getPaymentStatus(orderId: UUID): Payment {
        val order = orderGateway.findById(orderId) ?: throw ReasonCodeException(ReasonCode.ORDER_NOT_FOUND)

        return order.payment?.let { paymentGateway.findById(it.id) } ?: throw ReasonCodeException(ReasonCode.PAYMENT_NOT_FOUND)
    }
}
