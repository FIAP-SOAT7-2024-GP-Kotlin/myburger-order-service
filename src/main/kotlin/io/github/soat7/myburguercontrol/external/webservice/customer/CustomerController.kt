package io.github.soat7.myburguercontrol.external.webservice.customer

import io.github.soat7.myburguercontrol.adapters.controller.CustomerHandler
import io.github.soat7.myburguercontrol.external.webservice.customer.api.CustomerCreationRequest
import io.github.soat7.myburguercontrol.external.webservice.customer.api.CustomerResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController("customer-controller")
@RequestMapping(
    path = ["customers"],
    produces = [MediaType.APPLICATION_JSON_VALUE],
)
@CrossOrigin(origins = ["*"], allowedHeaders = ["*"])
@SecurityRequirement(name = "Bearer Authentication")
class CustomerController(
    private val customerHandler: CustomerHandler,
) {

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(
        tags = ["1 - Jornada de Cliente"],
        summary = "Utilize esta rota para criar um novo cliente",
        description = "Utilize esta rota para criar um novo cliente",
    )
    fun createCustomer(@RequestBody request: CustomerCreationRequest): ResponseEntity<CustomerResponse> =
        ResponseEntity.ok().body(customerHandler.createCustomer(request))

    @GetMapping("/{id}")
    @Operation(
        tags = ["99 - Adminstrativo"],
        summary = "Utilize esta rota para encontrar um cliente utilizando o identificador na base de dados",
        description = "Utilize esta rota para encontrar um cliente utilizando o identificador na base de dados",
    )
    fun findCustomerById(@PathVariable("id") id: UUID): ResponseEntity<CustomerResponse> =
        customerHandler.findCustomerById(id)?.let {
            ResponseEntity.ok().body(it)
        } ?: ResponseEntity.notFound().build()

    @GetMapping
    @Operation(
        tags = ["1 - Jornada de Cliente"],
        summary = "Utilize esta rota para encontrar um cliente pelo CPF",
        description = "Utilize esta rota para encontrar um cliente pelo CPF",
    )
    fun findCustomerByCpf(@RequestParam("cpf") cpf: String): ResponseEntity<CustomerResponse> =
        customerHandler.findCustomerByCpf(cpf)?.let {
            ResponseEntity.ok().body(it)
        } ?: ResponseEntity.notFound().build()
}
