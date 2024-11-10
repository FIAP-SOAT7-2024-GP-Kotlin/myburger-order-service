package io.github.soat7.myburguercontrol.config

import io.github.soat7.myburguercontrol.domain.usecase.CustomerUseCase
import io.github.soat7.myburguercontrol.domain.usecase.OrderUseCase
import io.github.soat7.myburguercontrol.external.db.customer.CustomerGateway
import io.github.soat7.myburguercontrol.external.db.order.OrderGateway
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(MercadoPagoProperties::class)
class BeanConfiguration {

    @Bean
    fun orderUseCase(
        orderGateway: OrderGateway,
        customerUseCase: CustomerUseCase,
    ) = OrderUseCase(
        orderGateway = orderGateway,
        customerUseCase = customerUseCase,
    )

    @Bean
    fun customerUseCase(
        customerGateway: CustomerGateway,
    ) = CustomerUseCase(
        customerGateway = customerGateway,
    )
}
