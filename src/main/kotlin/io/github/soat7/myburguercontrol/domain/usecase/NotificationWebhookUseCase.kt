package io.github.soat7.myburguercontrol.domain.usecase

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.soat7.myburguercontrol.config.MercadoPagoProperties
import io.github.soat7.myburguercontrol.exception.ReasonCode
import io.github.soat7.myburguercontrol.exception.ReasonCodeException
import io.github.soat7.myburguercontrol.external.webservice.notification.api.WebhookEvent
import io.github.soat7.myburguercontrol.external.webservice.notification.api.WebhookPaymentResponse
import org.apache.commons.codec.digest.HmacUtils
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.web.client.RestTemplate

private val logger: KLogger = KotlinLogging.logger {}

class NotificationWebhookUseCase(
    private val mercadoPagoProperties: MercadoPagoProperties,
    private val paymentUseCase: PaymentUseCase,
    private val objectMapper: ObjectMapper = ObjectMapper(),
) {

    fun processWebhook(headerEvent: Map<String, String>, bodyEvent: String): Boolean {
        logger.debug { "Process Webhook: $headerEvent $bodyEvent" }
        val validatedEvent: Boolean?
        val webhookEvent = objectMapper.readValue(bodyEvent, WebhookEvent::class.java)

        try {
            validatedEvent = validateWebhookEvent(headerEvent, webhookEvent)
        } catch (ex: Exception) {
            logger.error(ex) { "Error while checking event" }
            throw ReasonCodeException(ReasonCode.UNEXPECTED_ERROR, ex)
        }

        if (!validatedEvent) {
            return false
        }

        if (webhookEvent.action == "payment.updated") {
            val mercadoPagoPayment = getWebhookPaymentDetail(webhookEvent.data.id)
            paymentUseCase.updatePayment(
                mercadoPagoPayment.externalReference.toString(),
                mercadoPagoPayment.status,
            )
            return true
        } else {
            return false
        }
    }

    private fun validateWebhookEvent(header: Map<String, String>, body: WebhookEvent): Boolean {
        val xRequestId = header["x-request-id"]
        val xSignature = header["x-signature"]
        val xDataID = body.data.id

        val partsSignature = xSignature?.split(",")
        val signatureValues = mutableMapOf<String, String>()
        partsSignature?.forEach { part ->
            val (key, value) = part.split("=")
            signatureValues[key] = value
        }

        val ts = signatureValues["ts"]
        val v1 = signatureValues["v1"]
        val signedTemplate = "id:$xDataID;request-id:$xRequestId;ts:$ts;"
        val cyphedSignature: String = HmacUtils("HmacSHA256", mercadoPagoProperties.webhookKey).hmacHex(signedTemplate)

        return cyphedSignature == v1
    }

    private fun getWebhookPaymentDetail(paymentId: String): WebhookPaymentResponse {
        val url = "${mercadoPagoProperties.merchantURL}/$paymentId"
        val restTemplate = RestTemplate()
        val headers = HttpHeaders()
        headers.setBearerAuth(mercadoPagoProperties.accessToken)

        val requestEntity = HttpEntity<Any>(headers)

        val response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            requestEntity,
            String::class.java,
        )

        val json = response.body.toString()

        val webhookPaymentResponse = objectMapper.readValue(json, WebhookPaymentResponse::class.java)

        return webhookPaymentResponse
    }
}
