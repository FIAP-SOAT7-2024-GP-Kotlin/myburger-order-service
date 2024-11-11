package io.github.soat7.myburguercontrol.config

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import io.github.soat7.myburguercontrol.domain.usecase.CustomerUseCase
import io.github.soat7.myburguercontrol.domain.usecase.OrderUseCase
import io.github.soat7.myburguercontrol.external.db.customer.CustomerGateway
import io.github.soat7.myburguercontrol.external.db.order.OrderGateway
import org.bson.UuidRepresentation
import org.springframework.boot.autoconfigure.mongo.MongoProperties
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Profile
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.convert.MappingMongoConverter

@TestConfiguration
@Profile("test")
class BeanConfiguration(
    private val mongoProperties: MongoProperties,
) {

    @Bean
    fun mongoDbTemplate(mongoClient: MongoClient, mappingMongoConverter: MappingMongoConverter) =
        MongoTemplate(mongoClient, mongoProperties.database)

    @Bean
    fun mongoClient(): MongoClient = MongoClients.create(buildMongoClientSettings())

    private fun buildMongoClientSettings(): MongoClientSettings {
        return MongoClientSettings.builder()
            .applyConnectionString(ConnectionString(mongoProperties.uri))
            .uuidRepresentation(UuidRepresentation.STANDARD)
            .build()
    }

    @Bean
    fun customerUseCase(
        customerGateway: CustomerGateway,
    ) = CustomerUseCase(
        customerGateway = customerGateway,
    )

    @Bean
    fun orderUseCase(
        orderGateway: OrderGateway,
        customerUseCase: CustomerUseCase,
    ) = OrderUseCase(
        orderGateway = orderGateway,
        customerUseCase = customerUseCase,
    )
}
