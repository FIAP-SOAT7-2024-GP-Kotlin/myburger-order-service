package io.github.soat7.myburguercontrol.domain.usecase

import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.soat7.myburguercontrol.domain.entities.Customer
import io.github.soat7.myburguercontrol.exception.ReasonCode
import io.github.soat7.myburguercontrol.exception.ReasonCodeException
import io.github.soat7.myburguercontrol.external.db.customer.CustomerGateway
import org.springframework.stereotype.Service
import java.util.UUID

private val logger = KotlinLogging.logger {}

@Service
class CustomerUseCase(
    private val customerGateway: CustomerGateway,
) {

    fun create(customer: Customer): Customer = try {
        logger.info { "Creating new customer with cpf: [${customer.cpf}]" }
        validateExistingCpf(customer.cpf)

        customerGateway.create(customer)
    } catch (ex: ReasonCodeException) {
        throw ex
    } catch (ex: Exception) {
        logger.error(ex) { "Error while creating customer" }
        throw ReasonCodeException(ReasonCode.UNEXPECTED_ERROR, ex)
    }

    fun findCustomerById(id: UUID): Customer? = try {
        logger.info { "Finding customer with id: [$id]" }
        customerGateway.findCustomerById(id)
    } catch (ex: Exception) {
        logger.error(ex) { "Error while finding customer by id" }
        throw ReasonCodeException(ReasonCode.UNEXPECTED_ERROR, ex)
    }

    fun findCustomerByCpf(cpf: String): Customer? = try {
        logger.info { "Finding customer with cpf: [$cpf]" }
        customerGateway.findCustomerByCpf(cpf)
    } catch (ex: Exception) {
        logger.error(ex) { "Error while finding customer by cpf" }
        throw ReasonCodeException(ReasonCode.UNEXPECTED_ERROR, ex)
    }

    private fun validateExistingCpf(cpf: String) {
        if (findCustomerByCpf(cpf) != null) {
            throw ReasonCodeException(ReasonCode.CPF_ALREADY_REGISTERED)
        }
    }
}
