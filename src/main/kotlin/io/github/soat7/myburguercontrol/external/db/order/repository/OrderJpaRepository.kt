package io.github.soat7.myburguercontrol.external.db.order.repository

import io.github.soat7.myburguercontrol.external.db.order.entity.OrderEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.UUID

interface OrderJpaRepository : JpaRepository<OrderEntity, UUID> {

    fun findByCustomerId(customerId: UUID): List<OrderEntity>

    fun findAllByStatusOrderByCreatedAtAsc(status: String, pageable: Pageable): Page<OrderEntity>

    @Query(
        "SELECT o FROM OrderEntity o " +
            "WHERE o.status != 'FINISHED' " +
            "ORDER BY " +
            "CASE WHEN o.status = 'READY' THEN 1 " +
            "     WHEN o.status = 'IN_PROGRESS' THEN 2 " +
            "     WHEN o.status = 'RECEIVED' THEN 3 " +
            "     ELSE 4 END, " +
            "o.createdAt ASC",
    )
    override fun findAll(pageable: Pageable): Page<OrderEntity>
}
