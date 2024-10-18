package io.github.soat7.myburguercontrol.external.webservice.order.api

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import java.util.UUID

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class OrderCreationRequest(
    @NotBlank
    val customerCpf: String,
    @NotEmpty
    val items: List<OrderItem>,
) {
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    data class OrderItem(
        val productId: UUID,
        val quantity: Int,
        val comment: String? = null,
    )
}
