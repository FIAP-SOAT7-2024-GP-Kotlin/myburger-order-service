package io.github.soat7.myburguercontrol.adapters.controller

import io.github.soat7.myburguercontrol.adapters.mapper.toDomain
import io.github.soat7.myburguercontrol.adapters.mapper.toResponse
import io.github.soat7.myburguercontrol.domain.usecase.CustomerUseCase
import io.github.soat7.myburguercontrol.external.webservice.customer.api.CustomerCreationRequest
import java.util.UUID

class CustomerHandler(
    private val customerUseCase: CustomerUseCase,
) {

    fun createCustomer(request: CustomerCreationRequest) =
        customerUseCase.create(request.toDomain()).toResponse()

    fun findCustomerById(id: UUID) = customerUseCase.findCustomerById(id)?.toResponse()

    fun findCustomerByCpf(cpf: String) = customerUseCase.findCustomerByCpf(cpf)?.toResponse()
}
