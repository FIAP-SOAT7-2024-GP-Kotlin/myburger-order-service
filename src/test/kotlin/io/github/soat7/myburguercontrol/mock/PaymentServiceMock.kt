package io.github.soat7.myburguercontrol.mock

import io.github.soat7.myburguercontrol.container.MockServerContainer
import org.mockserver.matchers.Times
import org.mockserver.model.HttpRequest
import org.mockserver.model.HttpResponse
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import java.util.UUID

object PaymentServiceMock {

    fun sendPaymentRequest(
        responseStatus: HttpStatus = HttpStatus.OK,
        times: Int = 1,
    ) {
        val response = paymentResponse()
        MockServerContainer.client.`when`(
            HttpRequest.request()
                .withMethod(HttpMethod.POST.name())
                .withPath("/payment-integration"),
            Times.exactly(times),
        ).respond(
            HttpResponse.response()
                .withStatusCode(responseStatus.value())
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody(response),
        )
    }

    private fun paymentResponse(): String {
        val context = mapOf(
            "qrData" to UUID.randomUUID().toString(),
            "inStoreOrderId" to UUID.randomUUID().toString(),
        )

        return jsonResource("payment-service/200-payment.json", context)
    }
}
