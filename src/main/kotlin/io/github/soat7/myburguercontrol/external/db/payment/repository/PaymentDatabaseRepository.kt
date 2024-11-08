package io.github.soat7.myburguercontrol.external.db.payment.repository

import io.github.soat7.myburguercontrol.external.db.payment.model.PaymentEntity
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class PaymentDatabaseRepository(
    private val mongoDbTemplate: MongoTemplate,
    @Value("\${spring.data.mongodb.database}")private val database: String,
) {

    fun create(entity: PaymentEntity) =
        mongoDbTemplate.insert(entity, database)

    fun findById(id: UUID): PaymentEntity? =
        mongoDbTemplate.findById(id, PaymentEntity::class.java, database)

    fun update(entity: PaymentEntity): PaymentEntity {
        return mongoDbTemplate.save(entity, database)
    }
}
