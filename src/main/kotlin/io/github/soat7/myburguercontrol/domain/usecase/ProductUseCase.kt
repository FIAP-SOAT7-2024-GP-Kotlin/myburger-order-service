package io.github.soat7.myburguercontrol.domain.usecase

import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.soat7.myburguercontrol.adapters.gateway.ProductRepository
import io.github.soat7.myburguercontrol.domain.entities.Product
import io.github.soat7.myburguercontrol.exception.ReasonCode
import io.github.soat7.myburguercontrol.exception.ReasonCodeException
import java.util.UUID

private val logger = KotlinLogging.logger {}

class ProductUseCase(
    private val productGateway: ProductRepository,
) {

    fun findById(id: UUID): Product? = try {
        logger.debug { "Finding product with id $id" }
        productGateway.findById(id)
    } catch (ex: Exception) {
        logger.error(ex) { "Error while finding product by type" }
        throw ReasonCodeException(ReasonCode.UNEXPECTED_ERROR, ex)
    }
}
