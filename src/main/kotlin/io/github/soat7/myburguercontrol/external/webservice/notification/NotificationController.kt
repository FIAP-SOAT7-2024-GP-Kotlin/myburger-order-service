package io.github.soat7.myburguercontrol.external.webservice.notification

import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.soat7.myburguercontrol.adapters.controller.NotificationHandler
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

private val logger = KotlinLogging.logger {}

@RestController("webhook-controller")
@RequestMapping(
    path = ["webhook"],
    produces = [MediaType.APPLICATION_JSON_VALUE],
)
@CrossOrigin(origins = ["*"], allowedHeaders = ["*"])
@SecurityRequirement(name = "Bearer Authentication")
class NotificationController(
    private val notificationHandler: NotificationHandler,
) {

    @PostMapping(
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        params = ["id", "topic"],
    )
    @Operation(
        tags = ["99 - Adminstrativo"],
        summary = "Rota utilizada para receber notificações Ipn",
        description = "Rota utilizada para receber notificações Ipn",
    )
    fun notificationIpn(
        @RequestParam("id") id: String,
        @RequestParam("topic") topic: String,
        @RequestHeader header: Map<String, String>,
        @RequestBody body: String,
    ): ResponseEntity<String> = run {
        logger.debug { "\nRecebeu notificação IPN \n$header \n$body" }
        val result = notificationHandler.processIpn(header, body)
        return if (result) {
            ResponseEntity.ok().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @PostMapping(
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        params = ["data.id", "type"],
    )
    @Operation(
        tags = ["99 - Adminstrativo"],
        summary = "Rota utilizada para receber notificações WebHook",
        description = "Rota utilizada para receber notificações WebHook",
    )
    fun notificationWebhook(
        @RequestParam("data.id") id: String,
        @RequestParam("type") topic: String,
        @RequestHeader header: Map<String, String>,
        @RequestBody body: String,
    ): ResponseEntity<String> = run {
        logger.debug { "\nRecebeu notificação WebHook \n$header \n$body" }
        val result = notificationHandler.processWebhook(header, body)
        return if (result) {
            ResponseEntity.ok().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}
