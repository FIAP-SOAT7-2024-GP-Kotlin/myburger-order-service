package io.github.soat7.myburguercontrol.external.webservice.config

import io.github.soat7.myburguercontrol.adapters.controller.OrderHandler
import io.github.soat7.myburguercontrol.domain.usecase.OrderUseCase
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class WebServiceBeanConfiguration {

    @Bean
    fun orderHandler(orderUseCase: OrderUseCase) =
        OrderHandler(orderUseCase = orderUseCase)
}
