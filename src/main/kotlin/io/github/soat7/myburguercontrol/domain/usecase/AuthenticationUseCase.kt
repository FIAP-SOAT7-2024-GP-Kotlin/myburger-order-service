package io.github.soat7.myburguercontrol.domain.usecase

import io.github.soat7.myburguercontrol.config.JwtProperties
import io.github.soat7.myburguercontrol.external.webservice.auth.api.AuthenticationRequest
import io.github.soat7.myburguercontrol.external.webservice.auth.api.AuthenticationResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import java.util.Date

class AuthenticationUseCase(
    private val authManager: AuthenticationManager,
    private val userDetailsService: CustomUserDetailsUseCase,
    private val tokenUseCase: TokenUseCase,
    private val jwtProperties: JwtProperties,
) {

    fun authenticate(request: AuthenticationRequest): AuthenticationResponse {
        authManager.authenticate(
            UsernamePasswordAuthenticationToken(
                request.cpf,
                request.password,
            ),
        )
        val user = userDetailsService.loadUserByUsername(request.cpf)

        val accessToken = tokenUseCase.generate(
            userDetails = user,
            expirationDate = Date(System.currentTimeMillis() + jwtProperties.accessTokenExpiration),
        )

        return AuthenticationResponse(accessToken)
    }
}
