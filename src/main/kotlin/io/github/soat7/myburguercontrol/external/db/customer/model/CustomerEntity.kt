package io.github.soat7.myburguercontrol.external.db.customer.model

import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant
import java.util.UUID

@Document
data class CustomerEntity(
    val id: UUID,
    val cpf: String,
    val name: String? = null,
    val email: String? = null,
    val createdAt: Instant,
    val updatedAt: Instant?,
)
