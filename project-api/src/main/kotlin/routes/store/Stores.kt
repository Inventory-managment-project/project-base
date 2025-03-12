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
import mx.unam.fciencias.ids.eq1.service.store.StoreService
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