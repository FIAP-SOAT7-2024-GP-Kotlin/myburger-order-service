package io.github.soat7.myburguercontrol.external.db.customer.repository

import io.github.soat7.myburguercontrol.external.db.customer.entity.CustomerEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface CustomerJpaRepository : JpaRepository<CustomerEntity, UUID> {

    fun findByCpf(cpf: String): CustomerEntity?
}
