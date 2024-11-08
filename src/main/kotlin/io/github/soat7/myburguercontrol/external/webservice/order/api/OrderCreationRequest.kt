package io.github.soat7.myburguercontrol.external.webservice.order.api

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import jakarta.validation.constraints.NotEmpty
import java.math.BigDecimal
import java.util.UUID

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class OrderCreationRequest(
    val customerCpf: String? = null,
    @NotEmpty(message = "one or more items must be provided")
    val items: List<OrderItem>,
) {
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    data class OrderItem(
        val productId: UUID,
        val price: BigDecimal,
        val quantity: Int,
        val comment: String? = null,
    )
}
