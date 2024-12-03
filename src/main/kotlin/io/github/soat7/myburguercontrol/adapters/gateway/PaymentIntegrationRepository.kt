package io.github.soat7.myburguercontrol.adapters.gateway

import io.github.soat7.myburguercontrol.domain.entities.Order

interface PaymentIntegrationRepository {

    fun requestPayment(order: Order)
}
