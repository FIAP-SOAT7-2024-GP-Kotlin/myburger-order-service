package io.github.soat7.myburguercontrol.fixtures

import io.github.soat7.myburguercontrol.external.webservice.auth.api.AuthenticationRequest

object AuthFixtures {

    fun mockAuthCreationRequest(cpf: String, password: String) = AuthenticationRequest(
        cpf = cpf,
        password = password,
    )
}
