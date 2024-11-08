package io.github.soat7.myburguercontrol.external.thirdparty.payment

import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.soat7.myburguercontrol.adapters.gateway.PaymentIntegrationRepository
import io.github.soat7.myburguercontrol.adapters.mapper.toPaymentRequest
import io.github.soat7.myburguercontrol.domain.entities.Order
import io.github.soat7.myburguercontrol.exception.ReasonCode
import io.github.soat7.myburguercontrol.exception.ReasonCodeException
import io.github.soat7.myburguercontrol.external.thirdparty.payment.api.QRCodeData
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClientResponseException
import org.springframework.web.client.RestTemplate

private val logger = KotlinLogging.logger {}

@Component
class PaymentIntegrationClient(
    @Value("\${third-party.payment-service.url}") private val paymentServiceUrl: String,
    @Qualifier("paymentRestTemplate") private val paymentRestTemplate: RestTemplate,
) : PaymentIntegrationRepository {

    override fun requestQRCodeDataForPayment(order: Order): QRCodeData {
        try {
            val orderToRequest = order.toPaymentRequest()

            logger.info { "Requesting PaymentData with [payload: $orderToRequest]" }

            val response = paymentRestTemplate.exchange(
                paymentServiceUrl,
                HttpMethod.POST,
                HttpEntity(orderToRequest),
                QRCodeData::class.java,
            ).also { logger.info { "Received response ${it.body}" } }

            if (response.statusCode.is2xxSuccessful) {
                response.body?.let {
                    return it
                } ?: run {
                    throw ReasonCodeException(ReasonCode.PAYMENT_INTEGRATION_ERROR)
                }
            } else {
                throw ReasonCodeException(ReasonCode.UNEXPECTED_ERROR)
            }
        } catch (ex: RestClientResponseException) {
            logger.warn { "Integration error" }.also { throw ex }
        }
    }
}
