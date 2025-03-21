package mx.unam.fciencias.ids.eq1.routes.store

import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import mx.unam.fciencias.ids.eq1.model.store.CreateStoreRequest
import mx.unam.fciencias.ids.eq1.service.users.UserService
import io.ktor.server.response.*
import mx.unam.fciencias.ids.eq1.routes.getRequestEmailOrRespondBadRequest
import mx.unam.fciencias.ids.eq1.routes.store.sales.sales
import mx.unam.fciencias.ids.eq1.service.store.StoreService
import org.koin.ktor.ext.inject


fun Route.createStores() {
    authenticate("auth-jwt") {
        route("createStore") {
            post {
                val createStoreRequest = call.receive<CreateStoreRequest>()
                val userEmail = call.getRequestEmailOrRespondBadRequest() ?: return@post
                val userService by call.inject<UserService>()
                val user = userService.getUserByEmail(userEmail)
                    ?: return@post call.respond(HttpStatusCode.Forbidden)
                val storeService by call.application.inject<StoreService>()
                storeService.createStore(createStoreRequest, user)
                call.respond(HttpStatusCode.Created)
            }
        }
    }
}

fun Route.storeRoutes() {
    authenticate("auth-jwt") {
        route("/stores") {
            products()
            sales()
            route("/{storeId}") {
                get {
                    val storeId = call.parameters["storeId"]?.toIntOrNull() ?: return@get call.respond(HttpStatusCode.BadRequest)
                    val storeService by call.inject<StoreService>()
                    val store = storeService.getStoreById(storeId) ?: return@get call.respond(HttpStatusCode.NotFound)
                    call.respond(HttpStatusCode.OK, store)
                }
            }
            get {
                val email = call.getRequestEmailOrRespondBadRequest() ?: return@get
                val userService by call.application.inject<UserService>()
                val ownerId = userService.getUserByEmail(email)?.id ?: return@get call.respond(HttpStatusCode.NotFound)
                if (userService.getUserByEmail(email)?.id != ownerId) {
                    return@get call.respond(HttpStatusCode.Forbidden)
                }
                val storeService by call.inject<StoreService>()
                val stores = storeService.getStoresByOwner(ownerId)
                call.respond(HttpStatusCode.OK, stores)
            }
            get("/owner") {
                val email = call.getRequestEmailOrRespondBadRequest() ?: return@get
                val userService by call.application.inject<UserService>()
                val ownerId = userService.getUserByEmail(email)?.id ?: return@get
                if (userService.getUserByEmail(email)?.id != ownerId) {
                    return@get call.respond(HttpStatusCode.Forbidden)
                }
                val storeService by call.inject<StoreService>()
                val stores = storeService.getStoresByOwner(ownerId)
                call.respond(HttpStatusCode.OK, stores)
            }
        }
    }
}