package io.github.soat7.myburguercontrol.external.webservice.auth

import io.github.soat7.myburguercontrol.adapters.controller.AuthenticationHandler
import io.github.soat7.myburguercontrol.external.webservice.auth.api.AuthenticationRequest
import io.github.soat7.myburguercontrol.external.webservice.auth.api.AuthenticationResponse
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(
    path = ["/auth"],
    produces = [MediaType.APPLICATION_JSON_VALUE],
    consumes = [MediaType.APPLICATION_JSON_VALUE],
)
class AuthenticationController(
    private val authenticationHandler: AuthenticationHandler,
) {

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(
        tags = ["0 - Jornada de Autenticação"],
        summary = "Utilize esta rota para autenticar um usuário já criado",
        description = "Utilize esta rota para autenticar um usuário já criado",
        operationId = "auth_3",
    )
    fun authenticate(@RequestBody authenticationRequest: AuthenticationRequest): ResponseEntity<AuthenticationResponse> =
        ResponseEntity.ok(authenticationHandler.authenticate(authenticationRequest))
}
