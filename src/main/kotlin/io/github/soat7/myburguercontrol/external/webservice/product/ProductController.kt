package io.github.soat7.myburguercontrol.external.webservice.product

import io.github.soat7.myburguercontrol.adapters.controller.ProductHandler
import io.github.soat7.myburguercontrol.external.webservice.common.PaginatedResponse
import io.github.soat7.myburguercontrol.external.webservice.product.api.ProductCreationRequest
import io.github.soat7.myburguercontrol.external.webservice.product.api.ProductResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController("product-controller")
@RequestMapping(
    path = ["products"],
    produces = [MediaType.APPLICATION_JSON_VALUE],
)
@CrossOrigin(origins = ["*"], allowedHeaders = ["*"])
@SecurityRequirement(name = "Bearer Authentication")
class ProductController(
    private val productHandler: ProductHandler,
) {

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(
        tags = ["99 - Adminstrativo"],
        summary = "Utilize esta rota para cadastrar um novo produto",
        description = "Utilize esta rota para cadastrar um novo produto",
    )
    fun createProduct(@RequestBody request: ProductCreationRequest): ResponseEntity<ProductResponse> =
        ResponseEntity.ok(productHandler.create(request))

    @DeleteMapping(path = ["/{id}"])
    @Operation(
        tags = ["99 - Adminstrativo"],
        summary = "Utilize esta rota para apagar um produto utilizando o identificador na base de dados",
        description = "Utilize esta rota para apagar um produto utilizando o identificador na base de dados",
    )
    fun deleteProduct(@PathVariable("id") id: UUID): ResponseEntity<Void> = run {
        productHandler.delete(id)
        ResponseEntity.noContent().build()
    }

    @GetMapping(path = ["/{id}"])
    @Operation(
        tags = ["99 - Adminstrativo"],
        summary = "Utilize esta rota para encontrar um produto utilizando o identificador na base de dados",
        description = "Utilize esta rota para encontrar um produto utilizando o identificador na base de dados",
    )
    fun getProductById(@PathVariable("id") id: UUID): ResponseEntity<ProductResponse> =
        productHandler.getById(id)?.let {
            ResponseEntity.ok(it)
        } ?: ResponseEntity.notFound().build()

    @GetMapping("/type")
    @Operation(
        tags = ["2 - Jornada do Pedido"],
        summary = "Utilize esta rota para buscar todos os produtos cadastrados por categoria",
        description = "Utilize esta rota para buscar todos os produtos cadastrados por categoria",
    )
    fun getProductByType(@RequestParam type: String): ResponseEntity<List<ProductResponse>> =
        ResponseEntity.ok(productHandler.getByProductType(type))

    @GetMapping
    @Operation(
        tags = ["2 - Jornada do Pedido"],
        summary = "Utilize esta rota para buscar todos os produtos cadastrados",
        description = "Utilize esta rota para buscar todos os produtos cadastrados",
    )
    fun findAll(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
    ): ResponseEntity<PaginatedResponse<ProductResponse>> = ResponseEntity.ok(productHandler.findAll(page, size))
}
