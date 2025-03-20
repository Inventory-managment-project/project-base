package mx.unam.fciencias.ids.eq1.routes.store

import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import mx.unam.fciencias.ids.eq1.model.store.CreateStoreRequest
import mx.unam.fciencias.ids.eq1.service.users.UserService
import io.ktor.server.response.*
import mx.unam.fciencias.ids.eq1.routes.getRequestEmailOrRespondBadRequest
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
    val storeService by inject<StoreService>()
    authenticate("auth-jwt") {
        route("/stores") {
            get("/owner/{ownerId}") {
                val email = call.getRequestEmailOrRespondBadRequest() ?: return@get
                val userService by call.application.inject<UserService>()
                val ownerId = call.parameters["ownerId"]?.toIntOrNull()
                    ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid owner ID")
                if (userService.getUserByEmail(email)?.id != ownerId) {
                    return@get call.respond(HttpStatusCode.Forbidden)
                }
                val stores = storeService.getStoresByOwner(ownerId)
                call.respond(HttpStatusCode.OK, stores)
            }
        }
    }
}