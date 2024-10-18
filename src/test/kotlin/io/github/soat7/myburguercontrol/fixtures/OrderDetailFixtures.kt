package io.github.soat7.myburguercontrol.fixtures

import io.github.soat7.myburguercontrol.domain.entities.OrderDetail
import io.github.soat7.myburguercontrol.domain.entities.Product

object OrderDetailFixtures {

    fun mockOrderDetail(
        cpf: String,
        product: Product = ProductFixtures.mockDomainProduct(),
    ) = OrderDetail(
        customerCpf = cpf,
        items = listOf(
            OrderDetail.OrderItemDetail(
                productId = product.id,
                quantity = 1,
            ),
        ),
    )
}
