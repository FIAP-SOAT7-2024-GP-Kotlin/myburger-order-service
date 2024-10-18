package io.github.soat7.myburguercontrol.domain.usecase

import io.github.soat7.myburguercontrol.domain.entities.User
import io.github.soat7.myburguercontrol.external.db.user.UserGateway
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.UUID

class UserUseCase(
    private val userGateway: UserGateway,
    private val encoder: PasswordEncoder,
) {

    fun create(user: User): User {
        user.password = encoder.encode(user.password)
        return userGateway.create(user)
    }

    fun findUserById(id: UUID): User? {
        return userGateway.findUserById(id)
    }

    fun findUserByCpf(cpf: String): User? {
        return userGateway.findUserByCpf(cpf)
    }

    fun findByAll(): List<User> {
        return userGateway.findByAll()
    }

    fun deleteByUUID(uuid: UUID): Boolean {
        val found = findUserById(uuid)
        if (found != null) {
            userGateway.deleteByUUID(uuid)
            return true
        } else {
            return false
        }
    }
}
