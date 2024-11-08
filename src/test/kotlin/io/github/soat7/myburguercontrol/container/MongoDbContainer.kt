package io.github.soat7.myburguercontrol.container

import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.utility.DockerImageName
import java.util.UUID

class MongoDbContainer : BeforeAllCallback {

    companion object {
        private val container =
            MongoDBContainer(DockerImageName.parse("mongo:7.0"))
                .apply {
                    withCreateContainerCmdModifier { it.withName("mongodb_my_burger_order_service-${UUID.randomUUID()}") }
                }

        private val log: Logger = LoggerFactory.getLogger(this::class.java)
    }

    override fun beforeAll(context: ExtensionContext?) {
        if (!container.isRunning) {
            container.start()
            log.info("Postgres container started with host [${container.host}]")
            System.setProperty("spring.data.mongodb.uri", container.replicaSetUrl)
            System.setProperty("spring.data.mongodb.database", "testdb")
            System.setProperty("spring.data.mongodb.collection.orders", "ordersTestCollection")
            System.setProperty("spring.data.mongodb.collection.customers", "customersTestCollection")
        }
    }
}
