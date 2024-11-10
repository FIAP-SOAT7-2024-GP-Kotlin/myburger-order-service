package io.github.soat7.myburguercontrol.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate
import java.time.Duration

@Configuration
class RestTemplateConfiguration {

    @Bean
    fun paymentRestTemplate(
        @Value("\${third-party.payment-service.connectionTimeout}") connectTimeout: Long,
        @Value("\${third-party.payment-service.readTimeout}") readTimeout: Long,
    ): RestTemplate {
        return RestTemplateBuilder()
            .setConnectTimeout(Duration.ofMillis(connectTimeout))
            .setReadTimeout(Duration.ofMillis(readTimeout))
            .build()
    }
}
