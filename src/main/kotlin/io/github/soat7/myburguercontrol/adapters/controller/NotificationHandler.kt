package io.github.soat7.myburguercontrol.adapters.controller

import io.github.soat7.myburguercontrol.domain.usecase.NotificationIpnUseCase
import io.github.soat7.myburguercontrol.domain.usecase.NotificationWebhookUseCase

class NotificationHandler(
    private val notificationWebhookUseCase: NotificationWebhookUseCase,
    private val notificationIpnUseCase: NotificationIpnUseCase,
) {
    fun processIpn(header: Map<String, String>, body: String) = notificationIpnUseCase.processIpn(header, body)

    fun processWebhook(header: Map<String, String>, body: String) = notificationWebhookUseCase.processWebhook(header, body)
}
