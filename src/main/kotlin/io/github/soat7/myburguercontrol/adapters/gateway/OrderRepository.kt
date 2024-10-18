package io.github.soat7.myburguercontrol.adapters.gateway

import io.github.soat7.myburguercontrol.domain.entities.Order
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface OrderRepository {
    fun create(order: Order): Order
    fun findByCustomerId(customerId: UUID): List<Order>
    fun findNewOrders(status: String, pageable: Pageable): Page<Order>
    fun update(order: Order): Order
    fun findById(orderId: UUID): Order?
    fun findAll(pageable: Pageable): Page<Order>
}
