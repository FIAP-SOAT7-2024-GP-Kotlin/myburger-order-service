package io.github.soat7.myburguercontrol.external.webservice.user.api

import io.github.soat7.myburguercontrol.domain.entities.enum.UserRole
import java.util.UUID

data class UserResponse(
    val id: UUID,
    val cpf: String,
    val role: UserRole,
)
