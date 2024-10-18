package io.github.soat7.myburguercontrol.adapters.controller

import io.github.soat7.myburguercontrol.domain.usecase.AuthenticationUseCase
import io.github.soat7.myburguercontrol.external.webservice.auth.api.AuthenticationRequest
import io.github.soat7.myburguercontrol.external.webservice.auth.api.AuthenticationResponse

class AuthenticationHandler(
    private val authenticationUseCase: AuthenticationUseCase,
) {

    fun authenticate(request: AuthenticationRequest): AuthenticationResponse =
        authenticationUseCase.authenticate(request)
}
