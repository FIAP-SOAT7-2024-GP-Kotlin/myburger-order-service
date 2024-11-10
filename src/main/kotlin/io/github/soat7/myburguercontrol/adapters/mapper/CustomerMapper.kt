package io.github.soat7.myburguercontrol.adapters.mapper

import io.github.soat7.myburguercontrol.domain.entities.Customer
import io.github.soat7.myburguercontrol.external.db.customer.model.CustomerEntity
import io.github.soat7.myburguercontrol.external.webservice.customer.api.CustomerCreationRequest
import io.github.soat7.myburguercontrol.external.webservice.customer.api.CustomerResponse
import java.time.Instant
import java.util.UUID

fun CustomerCreationRequest.toDomain() = Customer(
    id = UUID.randomUUID(),
    cpf = this.cpf,
    name = this.name,
    email = this.email,
)

fun CustomerEntity.toDomain() = Customer(
    id = this.id!!,
    cpf = this.cpf,
    name = this.name,
    email = this.email,
)

fun Customer.toPersistence() = CustomerEntity(
    id = this.id,
    cpf = this.cpf,
    name = this.name,
    email = this.email,
    createdAt = Instant.now(),
    updatedAt = Instant.now(),
)

fun Customer.toResponse() = CustomerResponse(
    id = this.id,
    cpf = this.cpf,
    name = this.name,
    email = this.email,
)
