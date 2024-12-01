package io.github.soat7.myburguercontrol.fixtures

import io.github.soat7.myburguercontrol.domain.entities.OrderDetail
import java.util.UUID

object OrderDetailFixtures {

    fun mockOrderDetail(
        customerId: UUID,
        id: UUID = UUID.randomUUID(),
    ) = OrderDetail(
        customerId = customerId,
        items = listOf(
            OrderDetail.OrderItemDetail(
                productId = id,
                quantity = 1,
                price = 5.99.toBigDecimal(),
            ),
        ),
    )
}
