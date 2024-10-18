package io.github.soat7.myburguercontrol.config

import io.github.soat7.myburguercontrol.adapters.gateway.PaymentIntegrationRepository
import io.github.soat7.myburguercontrol.domain.usecase.AuthenticationUseCase
import io.github.soat7.myburguercontrol.domain.usecase.CustomUserDetailsUseCase
import io.github.soat7.myburguercontrol.domain.usecase.CustomerUseCase
import io.github.soat7.myburguercontrol.domain.usecase.NotificationIpnUseCase
import io.github.soat7.myburguercontrol.domain.usecase.NotificationWebhookUseCase
import io.github.soat7.myburguercontrol.domain.usecase.OrderUseCase
import io.github.soat7.myburguercontrol.domain.usecase.PaymentUseCase
import io.github.soat7.myburguercontrol.domain.usecase.ProductUseCase
import io.github.soat7.myburguercontrol.domain.usecase.TokenUseCase
import io.github.soat7.myburguercontrol.domain.usecase.UserUseCase
import io.github.soat7.myburguercontrol.external.db.customer.CustomerGateway
import io.github.soat7.myburguercontrol.external.db.order.OrderGateway
import io.github.soat7.myburguercontrol.external.db.payment.PaymentGateway
import io.github.soat7.myburguercontrol.external.db.product.ProductGateway
import io.github.soat7.myburguercontrol.external.db.user.UserGateway
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
@EnableConfigurationProperties(MercadoPagoProperties::class)
class BeanConfiguration {

    @Bean
    fun authenticationUseCase(
        authenticationManager: AuthenticationManager,
        customUserDetailsUseCase: CustomUserDetailsUseCase,
        tokenUseCase: TokenUseCase,
        jwtProperties: JwtProperties,
    ) = AuthenticationUseCase(
        authManager = authenticationManager,
        userDetailsService = customUserDetailsUseCase,
        tokenUseCase = tokenUseCase,
        jwtProperties = jwtProperties,
    )

    @Bean
    fun customerUseCase(
        customerGateway: CustomerGateway,
    ) = CustomerUseCase(
        customerGateway = customerGateway,
    )

    @Bean
    fun customUserDetailsUseCase(
        userGateway: UserGateway,
    ) = CustomUserDetailsUseCase(
        userGateway = userGateway,
    )

    @Bean
    fun orderUseCase(
        orderGateway: OrderGateway,
        customerUseCase: CustomerUseCase,
        productUseCase: ProductUseCase,
        paymentUseCase: PaymentUseCase,
    ) = OrderUseCase(
        orderGateway = orderGateway,
        customerUseCase = customerUseCase,
        productUseCase = productUseCase,
    )

    @Bean
    fun paymentService(
        paymentIntegrationRepository: PaymentIntegrationRepository,
        paymentGateway: PaymentGateway,
        orderGateway: OrderGateway,
    ) = PaymentUseCase(
        paymentIntegrationRepository = paymentIntegrationRepository,
        paymentGateway = paymentGateway,
        orderGateway = orderGateway,
    )

    @Bean
    fun productService(
        productGateway: ProductGateway,
    ) = ProductUseCase(
        productGateway = productGateway,
    )

    @Bean
    fun tokenUseCase(
        jwtProperties: JwtProperties,
    ) = TokenUseCase(
        jwtProperties = jwtProperties,
    )

    @Bean
    fun userUseCase(
        userGateway: UserGateway,
        encoder: PasswordEncoder,
    ) = UserUseCase(
        userGateway = userGateway,
        encoder = encoder,
    )

    @Bean
    fun notificationWebHookUseCase(
        mercadoPagoProperties: MercadoPagoProperties,
        paymentUseCase: PaymentUseCase,
    ) = NotificationWebhookUseCase(
        mercadoPagoProperties,
        paymentUseCase,
    )

    @Bean
    fun notificationIpnUseCase(
        mercadoPagoProperties: MercadoPagoProperties,
        paymentUseCase: PaymentUseCase,
    ) = NotificationIpnUseCase(
        mercadoPagoProperties,
        paymentUseCase,
    )
}
