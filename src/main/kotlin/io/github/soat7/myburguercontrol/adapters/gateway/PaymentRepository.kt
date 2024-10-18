package io.github.soat7.myburguercontrol.adapters.gateway

import io.github.soat7.myburguercontrol.domain.entities.Payment
import java.util.UUID

interface PaymentRepository {
    fun create(payment: Payment): Payment
    fun update(payment: Payment): Payment
    fun findById(id: UUID): Payment?
}
