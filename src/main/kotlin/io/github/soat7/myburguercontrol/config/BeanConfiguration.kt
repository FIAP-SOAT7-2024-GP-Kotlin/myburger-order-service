package io.github.soat7.myburguercontrol.config

import io.github.soat7.myburguercontrol.domain.usecase.CustomerUseCase
import io.github.soat7.myburguercontrol.domain.usecase.OrderUseCase
import io.github.soat7.myburguercontrol.external.db.customer.CustomerGateway
import io.github.soat7.myburguercontrol.external.db.order.OrderGateway
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class BeanConfiguration {

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
