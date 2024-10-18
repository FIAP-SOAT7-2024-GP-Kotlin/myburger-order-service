package io.github.soat7.myburguercontrol.adapters.mapper

import io.github.soat7.myburguercontrol.domain.entities.User
import io.github.soat7.myburguercontrol.external.db.user.entity.UserEntity
import io.github.soat7.myburguercontrol.external.webservice.user.api.UserCreationRequest
import io.github.soat7.myburguercontrol.external.webservice.user.api.UserResponse
import java.util.UUID

fun UserCreationRequest.toDomain() = User(
    id = UUID.randomUUID(),
    cpf = this.cpf,
    password = this.password,
    role = this.role,
)

fun UserEntity.toDomain() = User(
    id = this.id,
    cpf = this.cpf,
    password = this.password,
    role = this.role,
)

fun User.toPersistence() = UserEntity(
    id = this.id,
    cpf = this.cpf,
    password = this.password,
    role = this.role,
)

fun User.toResponse() = UserResponse(
    id = this.id,
    cpf = this.cpf,
    role = this.role,
)
