package io.github.soat7.myburguercontrol.external.webservice.notification.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class IpnEvent(
    @JsonProperty("resource") val resource: String,
    @JsonProperty("topic") val topic: String,
)
