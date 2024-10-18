package io.github.soat7.myburguercontrol.domain.usecase

import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.soat7.myburguercontrol.domain.entities.Product
import io.github.soat7.myburguercontrol.exception.ReasonCode
import io.github.soat7.myburguercontrol.exception.ReasonCode.PRODUCT_NOT_FOUND
import io.github.soat7.myburguercontrol.exception.ReasonCodeException
import io.github.soat7.myburguercontrol.external.db.product.ProductGateway
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

private val logger = KotlinLogging.logger {}

class ProductUseCase(
    private val productGateway: ProductGateway,
) {

    fun create(product: Product): Product = try {
        logger.debug { "Saving new product $product" }
        productGateway.create(product)
    } catch (ex: Exception) {
        logger.error(ex) { "Error while creating product" }
        throw ReasonCodeException(ReasonCode.UNEXPECTED_ERROR, ex)
    }

    fun delete(id: UUID) {
        logger.debug { "Saving product with id $id" }
        val product = productGateway.findById(id) ?: throw ReasonCodeException(PRODUCT_NOT_FOUND)
        productGateway.delete(product)
    }

    fun findAll(pageable: Pageable): Page<Product> = try {
        logger.debug { "Finding all products" }
        productGateway.findAll(pageable)
    } catch (ex: Exception) {
        logger.error(ex) { "Error while listing products" }
        throw ReasonCodeException(ReasonCode.UNEXPECTED_ERROR, ex)
    }

    fun findById(id: UUID): Product? = try {
        logger.debug { "Finding product with id $id" }
        productGateway.findById(id)
    } catch (ex: Exception) {
        logger.error(ex) { "Error while finding product by type" }
        throw ReasonCodeException(ReasonCode.UNEXPECTED_ERROR, ex)
    }

    fun findByType(type: String): List<Product> = try {
        logger.debug { "Finding product with type $type" }
        productGateway.findByType(type)
    } catch (ex: Exception) {
        logger.error(ex) { "Error while finding product by id" }
        throw ReasonCodeException(ReasonCode.UNEXPECTED_ERROR, ex)
    }
}
