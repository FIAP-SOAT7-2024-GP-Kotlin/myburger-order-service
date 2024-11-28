package io.github.soat7.myburguercontrol.config

import io.github.soat7.myburguercontrol.adapters.gateway.PaymentIntegrationRepository
import io.github.soat7.myburguercontrol.domain.usecase.OrderUseCase
import io.github.soat7.myburguercontrol.domain.usecase.PaymentUseCase
import io.github.soat7.myburguercontrol.external.db.order.OrderGateway
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class BeanConfiguration {

    @Bean
    fun paymentUseCase(
        paymentIntegrationRepository: PaymentIntegrationRepository,
    ) = PaymentUseCase(
        paymentIntegrationRepository = paymentIntegrationRepository,
    )

    @Bean
    fun orderUseCase(
        orderGateway: OrderGateway,
        paymentUseCase: PaymentUseCase,
    ) = OrderUseCase(
        orderGateway = orderGateway,
        paymentUseCase = paymentUseCase,
    )
}
