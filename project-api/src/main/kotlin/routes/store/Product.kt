package mx.unam.fciencias.ids.eq1.routes.store

import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mx.unam.fciencias.ids.eq1.model.store.product.Product
import mx.unam.fciencias.ids.eq1.service.store.product.ProductService
import mx.unam.fciencias.ids.eq1.service.users.UserService
import org.koin.core.parameter.parametersOf
import org.koin.ktor.ext.inject


private suspend fun RoutingCall.verifyUser() : Boolean {
    val storeId = this.parameters["storeId"]?.toIntOrNull() ?: return false
    val userEmail = this.parameters["email"] ?: return false
    val userService by this.application.inject<UserService>()
    return userService.isOwner(userEmail, storeId)
}

private suspend fun RoutingCall.getStoreIdOrBadRequest() : Int? {
    val storeId = this.parameters["storeId"]?.toIntOrNull()
    if (storeId == null) this.respond(HttpStatusCode.NotFound)
    return storeId
}

private suspend fun RoutingCall.getProductIdOrBadRequest() : Int? {
    val storeId = this.parameters["productId"]?.toIntOrNull()
    if (storeId == null) this.respond(HttpStatusCode.NoContent, "Product not found")
    return storeId
}

fun Route.products() {
    authenticate("auth-jwt") {
        route("/{storeId}/products") {
            get {
                val storeId = call.getStoreIdOrBadRequest() ?: return@get
                if (!call.verifyUser()) return@get call.respond(HttpStatusCode.NotFound)
                val productService by call.application.inject<ProductService> { parametersOf(storeId) }
                call.respond(productService.getAllProducts())
            }
            post {
                val storeId = call.getStoreIdOrBadRequest() ?: return@post
                if (!call.verifyUser()) return@post call.respond(HttpStatusCode.NotFound)
                val products = call.receive<List<Product>>()
                val productService by call.application.inject<ProductService> { parametersOf(storeId) }
                val productsId = products.map { productService.addProduct(it) }
                call.respond(HttpStatusCode.Created, productsId)
            }
        }

        route("{storeId}/product") {
            route("{productId}") {
                get {
                    if (!call.verifyUser()) return@get call.respond(HttpStatusCode.NotFound)
                    val storeId = call.getStoreIdOrBadRequest() ?: return@get
                    val productId = call.getProductIdOrBadRequest() ?: return@get
                    val productService by call.application.inject<ProductService> { parametersOf(storeId) }
                    val product =
                        productService.getProductById(productId) ?: return@get call.respond(HttpStatusCode.NotFound)
                    call.respond(HttpStatusCode.OK, product)
                }
                delete {
                    if (!call.verifyUser()) return@delete call.respond(HttpStatusCode.NotFound)
                    val storeId = call.getStoreIdOrBadRequest() ?: return@delete
                    val productId = call.getProductIdOrBadRequest() ?: return@delete
                    val productService by call.application.inject<ProductService> { parametersOf(storeId) }
                    if (productService.deleteProduct(productId)) {
                        call.respond(HttpStatusCode.OK)
                    } else {
                        call.respond(HttpStatusCode.NotFound)
                    }
                }
            }
            post {
                val storeId = call.getStoreIdOrBadRequest() ?: return@post
                val product = call.receive<Product>()
                val productService by call.application.inject<ProductService> { parametersOf(storeId) }
                val productId = productService.addProduct(product)
                call.respond(HttpStatusCode.Created, mapOf("id" to productId))
            }
            put {
                val storeId = call.getStoreIdOrBadRequest() ?: return@put
                val updatedProduct = call.receive<Product>()
                val productService by call.application.inject<ProductService> { parametersOf(storeId) }
                if (productService.updateProduct(updatedProduct)) {
                    call.respond(HttpStatusCode.OK, updatedProduct)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }
        }
    }
}