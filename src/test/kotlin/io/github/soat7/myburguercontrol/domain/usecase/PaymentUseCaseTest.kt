package io.github.soat7.myburguercontrol.domain.usecase

import io.github.soat7.myburguercontrol.adapters.gateway.PaymentIntegrationRepository
import io.github.soat7.myburguercontrol.external.db.order.OrderGateway
import io.github.soat7.myburguercontrol.external.db.payment.PaymentGateway
import io.github.soat7.myburguercontrol.fixtures.CustomerFixtures.mockDomainCustomer
import io.github.soat7.myburguercontrol.fixtures.OrderFixtures.mockOrder
import io.github.soat7.myburguercontrol.fixtures.PaymentFixtures.mockPayment
import io.github.soat7.myburguercontrol.fixtures.PaymentFixtures.mockQRCode
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestMethodOrder
import org.junit.jupiter.api.assertDoesNotThrow
import java.util.UUID
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNotNull
import io.github.soat7.myburguercontrol.domain.entities.Order as OrderModel

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class PaymentUseCaseTest {

    private val paymentIntegrationRepository = mockk<PaymentIntegrationRepository>()
    private val paymentGateway = mockk<PaymentGateway>()
    private val orderGateway = mockk<OrderGateway>()
    private val service = PaymentUseCase(paymentIntegrationRepository, paymentGateway, orderGateway)

    @BeforeTest
    fun setUp() {
        clearMocks(paymentIntegrationRepository)
    }

    @Test
    @Order(1)
    fun `should try to request QRCode successfully using an external service`() {
        val order = mockOrder(mockDomainCustomer(cpf = "12312312312"))

        every { paymentIntegrationRepository.requestQRCodeDataForPayment(any<OrderModel>()) } returns mockQRCode(
            UUID.randomUUID().toString(),
        )
        every { orderGateway.findById(any()) } returns order
        every { orderGateway.update(any()) } returns order
        every { paymentGateway.findById(any()) } returns mockPayment()
        every { paymentGateway.create(any()) } returns mockPayment()
        every { paymentGateway.update(any()) } returns mockPayment()

        val response = assertDoesNotThrow {
            service.startPaymentRequest(order.id)
        }

        assertNotNull(response)
    }
}
