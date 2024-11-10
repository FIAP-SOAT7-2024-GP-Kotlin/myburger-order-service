package io.github.soat7.myburguercontrol.external.db.order.repository

import io.github.soat7.myburguercontrol.external.db.order.model.OrderEntity
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class OrderDatabaseRepository(
    private val mongoDbTemplate: MongoTemplate,
    @Value("\${spring.data.mongodb.collection.orders}") private val collection: String,
) {

    fun create(entity: OrderEntity) =
        mongoDbTemplate.insert(entity, collection)

    fun findById(id: UUID): OrderEntity? =
        mongoDbTemplate.findById(id, OrderEntity::class.java, collection)

    fun findByCustomerId(customerId: UUID): List<OrderEntity> {
        return mongoDbTemplate.find(
            Query(
                Criteria.where("customer_id").`is`(customerId),
            ),
            OrderEntity::class.java,
        )
    }

    fun findAllByStatusOrderByCreatedAtAsc(status: String, pageable: Pageable): Page<OrderEntity> {
        val query = Query(Criteria.where("status").`is`(status))
        query.with(pageable)
        if (!pageable.sort.isSorted) {
            query.with(Sort.by(Sort.Direction.ASC, "createdAt"))
        }

        val orders = mongoDbTemplate.find(query, OrderEntity::class.java)
        val total = mongoDbTemplate.count(query, OrderEntity::class.java)
        return PageImpl(orders, pageable, total)
    }

    fun update(entity: OrderEntity): OrderEntity? {
        val query = Query(Criteria.where("id").`is`(entity.id))
        val update = Update()
            .set("status", entity.status)
            .set("updated_at", entity.updatedAt)

        mongoDbTemplate.updateFirst(query, update, OrderEntity::class.java, collection)

        return mongoDbTemplate.findById(entity.id, OrderEntity::class.java, collection)
    }

    fun findAll(pageable: Pageable): Page<OrderEntity> {
        val query = Query().with(pageable)
        val orders = mongoDbTemplate.find(query, OrderEntity::class.java)
        val total = mongoDbTemplate.count(query, OrderEntity::class.java)
        return PageImpl(orders, pageable, total)
    }
}
