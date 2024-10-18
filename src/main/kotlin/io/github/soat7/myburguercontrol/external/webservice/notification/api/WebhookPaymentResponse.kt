package io.github.soat7.myburguercontrol.external.webservice.notification.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class WebhookPaymentResponse(
    @JsonProperty("status") val status: String,
    @JsonProperty("status_detail") val statusDetail: String,
    @JsonProperty("external_reference") val externalReference: String? = null,
)
