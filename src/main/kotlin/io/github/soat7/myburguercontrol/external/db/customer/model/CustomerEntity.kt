package io.github.soat7.myburguercontrol.external.db.customer.model

import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant
import java.util.UUID

@Document
data class CustomerEntity(
    var id: UUID? = null,
    var cpf: String,
    var name: String? = null,
    var email: String? = null,
    val createdAt: Instant,
    val updatedAt: Instant?,
)
