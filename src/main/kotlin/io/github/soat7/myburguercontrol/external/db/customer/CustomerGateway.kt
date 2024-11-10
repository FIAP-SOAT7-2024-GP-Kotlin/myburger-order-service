package io.github.soat7.myburguercontrol.external.db.customer

import io.github.soat7.myburguercontrol.adapters.gateway.CustomerRepository
import io.github.soat7.myburguercontrol.adapters.mapper.toDomain
import io.github.soat7.myburguercontrol.adapters.mapper.toPersistence
import io.github.soat7.myburguercontrol.domain.entities.Customer
import io.github.soat7.myburguercontrol.external.db.customer.repository.CustomerDatabaseRepository
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class CustomerGateway(
    private val repository: CustomerDatabaseRepository,
) : CustomerRepository {

    override fun create(customer: Customer): Customer =
        repository.create(customer.toPersistence()).toDomain()

    override fun findCustomerByCpf(cpf: String): Customer? =
        repository.findByCpf(cpf)?.toDomain()

    override fun findCustomerById(id: UUID): Customer? =
        repository.findById(id)?.toDomain()
}
