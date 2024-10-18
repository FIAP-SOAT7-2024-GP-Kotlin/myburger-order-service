package io.github.soat7.myburguercontrol.adapters.controller

import io.github.soat7.myburguercontrol.adapters.mapper.toDomain
import io.github.soat7.myburguercontrol.adapters.mapper.toResponse
import io.github.soat7.myburguercontrol.domain.usecase.UserUseCase
import io.github.soat7.myburguercontrol.external.webservice.user.api.UserCreationRequest
import java.util.UUID

class UserHandler(
    private val userUseCase: UserUseCase,
) {

    fun create(request: UserCreationRequest) = userUseCase.create(request.toDomain()).toResponse()

    fun findUserById(id: UUID) = userUseCase.findUserById(id)?.toResponse()

    fun findUserByCpf(cpf: String) = userUseCase.findUserByCpf(cpf)?.toResponse()
}
