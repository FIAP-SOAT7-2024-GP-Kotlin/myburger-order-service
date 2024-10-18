package io.github.soat7.myburguercontrol.adapters.controller

import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.soat7.myburguercontrol.adapters.mapper.toDomain
import io.github.soat7.myburguercontrol.adapters.mapper.toResponse
import io.github.soat7.myburguercontrol.domain.usecase.ProductUseCase
import io.github.soat7.myburguercontrol.external.webservice.common.PaginatedResponse
import io.github.soat7.myburguercontrol.external.webservice.product.api.ProductCreationRequest
import org.springframework.data.domain.PageRequest
import java.util.UUID

private val logger = KotlinLogging.logger {}

class ProductHandler(
    private val productUseCase: ProductUseCase,
) {

    fun create(request: ProductCreationRequest) = run {
        logger.debug { "Creating product" }
        productUseCase.create(request.toDomain()).toResponse()
    }

    fun getById(id: UUID) = run {
        logger.debug { "Getting product by id" }
        productUseCase.findById(id)?.toResponse()
    }

    fun getByProductType(productType: String) = run {
        logger.debug { "Getting product by product type [$productType]" }
        productUseCase.findByType(productType).map { it.toResponse() }
    }

    fun findAll(page: Int, size: Int) = run {
        val pageable = PageRequest.of(page, size)
        val response = productUseCase.findAll(pageable)
        PaginatedResponse(
            content = response.content.map { it.toResponse() },
            totalPages = response.totalPages,
            totalElements = response.totalElements,
            currentPage = response.number,
            pageSize = response.size,
        )
    }

    fun delete(id: UUID) {
        logger.debug { "Deleting product with id: [$id]" }
        productUseCase.delete(id)
    }
}
