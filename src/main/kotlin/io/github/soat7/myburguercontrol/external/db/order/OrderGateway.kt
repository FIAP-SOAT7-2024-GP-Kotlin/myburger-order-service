package io.github.soat7.myburguercontrol.external.db.order

import io.github.soat7.myburguercontrol.adapters.gateway.OrderRepository
import io.github.soat7.myburguercontrol.adapters.mapper.toDomain
import io.github.soat7.myburguercontrol.adapters.mapper.toPersistence
import io.github.soat7.myburguercontrol.domain.entities.Order
import io.github.soat7.myburguercontrol.external.db.order.repository.OrderDatabaseRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class OrderGateway(
    private val repository: OrderDatabaseRepository,
) : OrderRepository {

    override fun create(order: Order): Order {
        val result = repository.create(order.toPersistence())
        return result.toDomain()
    }

    override fun findByCustomerId(customerId: UUID): List<Order> =
        repository.findByCustomerId(customerId).map { it.toDomain() }

    override fun findNewOrders(status: String, pageable: Pageable): Page<Order> =
        repository.findAllByStatusOrderByCreatedAtAsc(status, pageable)
            .map { it.toDomain() }

    override fun update(order: Order): Order {
        return repository.update(order.toPersistence())?.toDomain() ?: throw RuntimeException("Order not found")
    }

    override fun findById(orderId: UUID): Order? {
        return repository.findById(orderId)?.toDomain()
    }

    override fun findAll(pageable: Pageable): Page<Order> =
        repository.findAll(pageable).map {
            it.toDomain()
        }
}
