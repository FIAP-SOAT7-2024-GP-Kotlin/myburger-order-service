package io.github.soat7.myburguercontrol.domain.entities

import io.github.soat7.myburguercontrol.domain.entities.enum.UserRole
import java.util.UUID

data class User(
    val id: UUID,
    val cpf: String,
    var password: String,
    val role: UserRole,
)
