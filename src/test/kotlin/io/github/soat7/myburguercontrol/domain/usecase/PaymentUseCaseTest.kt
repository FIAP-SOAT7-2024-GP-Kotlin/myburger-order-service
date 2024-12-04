package io.github.soat7.myburguercontrol.domain.usecase

import io.github.soat7.myburguercontrol.adapters.gateway.PaymentIntegrationRepository
import io.github.soat7.myburguercontrol.fixtures.OrderFixtures.mockOrder
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestMethodOrder
import org.junit.jupiter.api.assertDoesNotThrow
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNotNull
import io.github.soat7.myburguercontrol.domain.entities.Order as OrderModel

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class PaymentUseCaseTest {

    private val paymentIntegrationRepository = mockk<PaymentIntegrationRepository>()
    private val service = PaymentUseCase(paymentIntegrationRepository)

    @BeforeTest
    fun setUp() {
        clearMocks(paymentIntegrationRepository)
    }

    @Test
    @Order(1)
    fun `should try to request payment successfully using an external service`() {
        val order = mockOrder()
        every { paymentIntegrationRepository.requestPayment(any<OrderModel>()) } returns ""

        val response = assertDoesNotThrow {
            service.sendPaymentRequest(order)
        }

        assertNotNull(response)
    }
}
