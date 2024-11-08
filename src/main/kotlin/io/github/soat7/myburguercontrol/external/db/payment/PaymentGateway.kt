package io.github.soat7.myburguercontrol.external.db.payment

import io.github.soat7.myburguercontrol.adapters.gateway.PaymentRepository
import io.github.soat7.myburguercontrol.adapters.mapper.toDomain
import io.github.soat7.myburguercontrol.adapters.mapper.toPersistence
import io.github.soat7.myburguercontrol.domain.entities.Payment
import io.github.soat7.myburguercontrol.exception.ReasonCode
import io.github.soat7.myburguercontrol.exception.ReasonCodeException
import io.github.soat7.myburguercontrol.external.db.payment.repository.PaymentDatabaseRepository
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class PaymentGateway(
    private val repository: PaymentDatabaseRepository,
) : PaymentRepository {

    override fun create(payment: Payment): Payment {
        return repository.create(payment.toPersistence()).toDomain()
    }

    override fun findById(id: UUID): Payment? {
        return repository.findById(id)?.toDomain()
    }

    override fun update(payment: Payment): Payment {
        findById(payment.id) ?: throw ReasonCodeException(ReasonCode.PAYMENT_NOT_FOUND)

        return repository.update(payment.toPersistence()).toDomain()
    }
}
