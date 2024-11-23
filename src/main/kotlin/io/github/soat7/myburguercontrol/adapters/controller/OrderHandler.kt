package io.github.soat7.myburguercontrol.adapters.controller

import io.github.soat7.myburguercontrol.adapters.mapper.toOrderDetails
import io.github.soat7.myburguercontrol.adapters.mapper.toResponse
import io.github.soat7.myburguercontrol.domain.entities.enum.OrderStatus
import io.github.soat7.myburguercontrol.domain.usecase.OrderUseCase
import io.github.soat7.myburguercontrol.external.webservice.common.PaginatedResponse
import io.github.soat7.myburguercontrol.external.webservice.order.api.OrderCreationRequest
import org.springframework.data.domain.PageRequest
import java.util.UUID

class OrderHandler(
    private val orderUseCase: OrderUseCase,
) {

    fun create(request: OrderCreationRequest) =
        orderUseCase.createOrder(request.toOrderDetails()).toResponse()

    fun findOrdersByCustomerCpf(cpf: String) =
        orderUseCase.findOrdersByCustomerCpf(cpf)
            .map { it.toResponse() }

    fun findOrderQueue(page: Int, size: Int) = run {
        val pageable = PageRequest.of(page, size)
        val response = orderUseCase.findQueuedOrders(pageable)

        PaginatedResponse(
            content = response.content.map { it.toResponse() },
            totalPages = response.totalPages,
            totalElements = response.totalElements,
            currentPage = response.number,
            pageSize = response.size,
        )
    }

    fun findAll(request: PageRequest) =
        orderUseCase.findAll(request).map { it.toResponse() }

    fun changeOrderStatus(status: OrderStatus, orderId: UUID) =
        orderUseCase.changeOrderStatus(status, orderId).toResponse()

    fun sendOrderPayment(orderId: UUID) {
        orderUseCase.sendOrderPayment(orderId = orderId)
    }
}
