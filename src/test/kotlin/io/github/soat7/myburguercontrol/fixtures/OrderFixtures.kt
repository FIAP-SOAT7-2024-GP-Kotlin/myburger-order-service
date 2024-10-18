package io.github.soat7.myburguercontrol.fixtures

import io.github.soat7.myburguercontrol.domain.entities.Customer
import io.github.soat7.myburguercontrol.domain.entities.Order
import io.github.soat7.myburguercontrol.domain.entities.OrderItem
import io.github.soat7.myburguercontrol.domain.entities.Payment
import io.github.soat7.myburguercontrol.domain.entities.enum.OrderStatus
import io.github.soat7.myburguercontrol.external.db.customer.entity.CustomerEntity
import io.github.soat7.myburguercontrol.external.db.order.entity.OrderEntity
import io.github.soat7.myburguercontrol.external.db.order.entity.OrderItemEntity
import io.github.soat7.myburguercontrol.external.db.payment.entity.PaymentEntity
import io.github.soat7.myburguercontrol.external.db.product.entity.ProductEntity
import io.github.soat7.myburguercontrol.fixtures.ProductFixtures.mockDomainProduct
import java.time.Instant
import java.util.UUID

object OrderFixtures {

    fun mockOrder(
        customer: Customer,
    ) = Order(
        id = UUID.randomUUID(),
        customer = customer,
        payment = Payment(),
    ).apply {
        val productId = UUID.randomUUID()
        this.items.map {
            OrderItem(
                id = productId,
                product = mockDomainProduct(description = "Product $productId"),
                quantity = 1,
            )
        }
    }

    fun mockOrderEntity(
        customerEntity: CustomerEntity,
        product: ProductEntity,
        paymentEntity: PaymentEntity,
        status: String? = null,
    ) = OrderEntity(
        id = UUID.randomUUID(),
        customer = customerEntity,
        status = status ?: OrderStatus.NEW.name,
        createdAt = Instant.now(),
        payment = paymentEntity,
    ).apply {
        this.items = listOf(
            OrderItemEntity(
                id = UUID.randomUUID(),
                this,
                product = product,
                quantity = 1,
            ),
        )
    }
}
