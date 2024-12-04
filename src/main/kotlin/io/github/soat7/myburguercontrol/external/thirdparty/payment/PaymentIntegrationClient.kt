package io.github.soat7.myburguercontrol.external.thirdparty.payment

import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.soat7.myburguercontrol.adapters.gateway.PaymentIntegrationRepository
import io.github.soat7.myburguercontrol.adapters.mapper.toPaymentRequest
import io.github.soat7.myburguercontrol.domain.entities.Order
import io.github.soat7.myburguercontrol.exception.ReasonCode
import io.github.soat7.myburguercontrol.exception.ReasonCodeException
import io.github.soat7.myburguercontrol.external.thirdparty.payment.api.PaymentResponse
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
    @Value("\${payment-service.url}") private val paymentServiceUrl: String,
    @Qualifier("paymentRestTemplate") private val paymentRestTemplate: RestTemplate,
) : PaymentIntegrationRepository {

    override fun requestPayment(order: Order): String {
        try {
            val orderToRequest = order.toPaymentRequest()

            logger.info { "Requesting PaymentData with [payload: $orderToRequest] at [url $paymentServiceUrl]"}

            val response = paymentRestTemplate.exchange(
                paymentServiceUrl,
                HttpMethod.POST,
                HttpEntity(orderToRequest),
                PaymentResponse::class.java,
            ).also { logger.info { "Received response ${it.body?.qrData}" } }

            if (response.body != null) {
                return response.body!!.qrData
            } else {
                throw ReasonCodeException(ReasonCode.PAYMENT_INTEGRATION_ERROR)            }
        } catch (ex: RestClientResponseException) {
            logger.error { "Integration error" }
            throw ex
        }
    }
}
