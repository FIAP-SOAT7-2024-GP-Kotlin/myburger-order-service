package io.github.soat7.myburguercontrol.adapters.mapper

import io.github.soat7.myburguercontrol.domain.entities.Order
import io.github.soat7.myburguercontrol.domain.entities.OrderDetail
import io.github.soat7.myburguercontrol.domain.entities.OrderItem
import io.github.soat7.myburguercontrol.domain.entities.enum.OrderStatus
import io.github.soat7.myburguercontrol.external.db.order.model.OrderEntity
import io.github.soat7.myburguercontrol.external.db.order.model.OrderItemEntity
import io.github.soat7.myburguercontrol.external.thirdparty.payment.api.Item
import io.github.soat7.myburguercontrol.external.thirdparty.payment.api.PaymentIntegrationRequest
import io.github.soat7.myburguercontrol.external.webservice.order.api.OrderCreationRequest
import io.github.soat7.myburguercontrol.external.webservice.order.api.OrderItemResponse
import io.github.soat7.myburguercontrol.external.webservice.order.api.OrderResponse
import java.time.Instant

fun OrderCreationRequest.toOrderDetails() = OrderDetail(
    customerCpf = this.customerCpf,
    items = this.items.map {
        OrderDetail.OrderItemDetail(
            productId = it.productId,
            price = it.price,
            quantity = it.quantity,
            comment = it.comment,
        )
    },
)

fun Order.toResponse() = OrderResponse(
    id = this.id,
    customerId = this.customerId,
    status = this.status,
    createdAt = this.createdAt,
    total = this.total,
).apply {
    this.items.addAll(
        this@toResponse.items.map {
            OrderItemResponse(
                productId = it.productId,
                quantity = it.quantity,
                comment = it.comment,
            )
        },
    )
}

fun Order.toPersistence() = OrderEntity(
    id = this.id,
    customerId = this.customerId,
    status = this.status.name,
    createdAt = this.createdAt,
    updatedAt = Instant.now(),
    paymentId = this.paymentId,
    items = this.items.map { it.toPersistence() },
)

fun OrderItem.toPersistence() =
    OrderItemEntity(
        productId = this.productId,
        quantity = this.quantity,
        comment = this.comment,
        price = this.price,
    )

fun OrderEntity.toDomain() = Order(
    id = this.id,
    customerId = this.customerId,
    status = OrderStatus.from(this.status),
    createdAt = this.createdAt,
    items = this.items.map { it.toDomain() as OrderItem },
    paymentId = this.paymentId,
)

fun OrderItemEntity.toDomain() = OrderItem(
    productId = this.productId,
    price = this.price,
    quantity = this.quantity,
    comment = this.comment,
)

fun Order.toPaymentRequest() = PaymentIntegrationRequest(
    description = "",
    externalReference = this.paymentId.toString(),
    items = items.map { it.toPaymentRequestItem() },
    totalAmount = this.total,
)

fun OrderItem.toPaymentRequestItem() = Item(
    unitPrice = this.price,
    quantity = this.quantity,
    unitMeasure = "Unit",
    totalAmount = this.price.multiply(this.quantity.toBigDecimal()),
)
