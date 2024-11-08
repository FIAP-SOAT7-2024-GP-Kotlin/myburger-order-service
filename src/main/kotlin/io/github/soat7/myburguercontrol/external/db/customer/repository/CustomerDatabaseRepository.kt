package io.github.soat7.myburguercontrol.external.db.customer.repository

import io.github.soat7.myburguercontrol.external.db.customer.model.CustomerEntity
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class CustomerDatabaseRepository(
    private val mongoDbTemplate: MongoTemplate,
    @Value("\${spring.data.mongodb.collection.customers}") private val collection: String,
) {
    fun create(entity: CustomerEntity) =
        mongoDbTemplate.insert(entity, collection)

    fun findByCpf(cpf: String): CustomerEntity? {
        return mongoDbTemplate.findOne(
            Query(
                Criteria.where("cpf").`is`(cpf),
            ),
            CustomerEntity::class.java,
            collection,
        )
    }

    fun findById(id: UUID): CustomerEntity? {
        return mongoDbTemplate.findById(id, CustomerEntity::class.java, collection)
    }
}
