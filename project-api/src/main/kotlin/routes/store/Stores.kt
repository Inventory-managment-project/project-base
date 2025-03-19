package mx.unam.fciencias.ids.eq1.routes.store

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import mx.unam.fciencias.ids.eq1.model.store.CreateStoreRequest
import mx.unam.fciencias.ids.eq1.service.users.UserService
import io.ktor.server.response.*
import mx.unam.fciencias.ids.eq1.model.store.product.Product
import mx.unam.fciencias.ids.eq1.service.store.StoreService
import mx.unam.fciencias.ids.eq1.service.store.product.ProductService
import org.koin.core.parameter.parametersOf
import org.koin.ktor.ext.inject


fun Application.createStores() {

    val storeService by inject<StoreService>()
    val userService by inject<UserService>()

    routing {
        authenticate("auth-jwt") {
            route("createStore") {
                post {
                    val createStoreRequest = call.receive<CreateStoreRequest>()
                    val userEmail = call.principal<JWTPrincipal>()?.payload?.getClaim("email")?.asString()
                        ?: return@post call.respond(HttpStatusCode.BadRequest)
                    val user = userService.getUserByEmail(userEmail)
                        ?: return@post call.respond(HttpStatusCode.Forbidden)
                    storeService.createStore(createStoreRequest, user)
                    call.respond(HttpStatusCode.Created)
                }
            }
        }
    }
}

fun Application.storeRoutes() {
    val storeService by inject<StoreService>()

    routing {
        route("/stores") {

            get("/owner/{ownerId}") {
                val ownerId = call.parameters["ownerId"]?.toIntOrNull()
                    ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid owner ID")

                val stores = storeService.getStoresByOwner(ownerId)
                call.respond(stores)
            }

            route("/{storeId}") {

                get {
                    val storeId = call.parameters["storeId"]?.toIntOrNull()
                        ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid store ID")

                    val store = storeService.getStoreById(storeId)
                        ?: return@get call.respond(HttpStatusCode.NotFound, "Store not found")

                    call.respond(store)
                }

                route("/products") {
                    get {
                        val storeId = call.parameters["storeId"]?.toIntOrNull()
                            ?: return@get call.respond(HttpStatusCode.BadRequest, "Store ID is required")

                        val productService by call.application.inject<ProductService> { parametersOf(storeId) }
                        call.respond(productService.getAllProducts())
                    }

                    post {
                        val storeId = call.parameters["storeId"]?.toIntOrNull()
                            ?: return@post call.respond(HttpStatusCode.BadRequest, "Store ID is required")

                        val product = call.receive<Product>()
                        val productService by call.application.inject<ProductService> { parametersOf(storeId) }
                        val productId = productService.addProduct(product)
                        call.respond(HttpStatusCode.Created, mapOf("id" to productId))
                    }
                }
            }
        }
    }
}