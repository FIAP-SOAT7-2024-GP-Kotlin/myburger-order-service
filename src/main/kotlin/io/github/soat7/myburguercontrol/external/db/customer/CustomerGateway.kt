package io.github.soat7.myburguercontrol.external.db.customer

import io.github.soat7.myburguercontrol.adapters.gateway.CustomerRepository
import io.github.soat7.myburguercontrol.adapters.mapper.toDomain
import io.github.soat7.myburguercontrol.adapters.mapper.toPersistence
import io.github.soat7.myburguercontrol.domain.entities.Customer
import io.github.soat7.myburguercontrol.external.db.customer.repository.CustomerJpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class CustomerGateway(
    private val repository: CustomerJpaRepository,
) : CustomerRepository {

    override fun create(customer: Customer): Customer = repository.save(customer.toPersistence()).toDomain()
    override fun findCustomerByCpf(cpf: String): Customer? = repository.findByCpf(cpf)?.toDomain()
    override fun findCustomerById(id: UUID): Customer? = repository.findByIdOrNull(id)?.toDomain()
}
