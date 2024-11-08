package io.github.soat7.myburguercontrol.external.db.payment.model

import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant
import java.util.UUID

@Document("payment")
data class PaymentEntity(
    val id: UUID,
    val status: String,
    val authorizationId: String?,
    val createdAt: Instant,
    val updatedAt: Instant,
)
