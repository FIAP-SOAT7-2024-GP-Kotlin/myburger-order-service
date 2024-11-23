package io.github.soat7.myburguercontrol.config

import io.github.soat7.myburguercontrol.adapters.gateway.PaymentIntegrationRepository
import io.github.soat7.myburguercontrol.domain.usecase.CustomerUseCase
import io.github.soat7.myburguercontrol.domain.usecase.OrderUseCase
import io.github.soat7.myburguercontrol.domain.usecase.PaymentUseCase
import io.github.soat7.myburguercontrol.external.db.customer.CustomerGateway
import io.github.soat7.myburguercontrol.external.db.order.OrderGateway
import io.github.soat7.myburguercontrol.external.db.payment.PaymentGateway
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
    fun paymentUseCase(
        paymentIntegrationRepository: PaymentIntegrationRepository,
        paymentGateway: PaymentGateway,
        orderGateway: OrderGateway,
    ) = PaymentUseCase(
        paymentIntegrationRepository = paymentIntegrationRepository,
        paymentGateway = paymentGateway,
        orderGateway = orderGateway,
    )

    @Bean
    fun orderUseCase(
        orderGateway: OrderGateway,
        customerUseCase: CustomerUseCase,
        paymentUseCase: PaymentUseCase,
    ) = OrderUseCase(
        orderGateway = orderGateway,
        customerUseCase = customerUseCase,
        paymentUseCase = paymentUseCase,
    )
}
