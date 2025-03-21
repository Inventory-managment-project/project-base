package mx.unam.fciencias.ids.eq1.routes.users

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mx.unam.fciencias.ids.eq1.service.users.UserService
import org.koin.ktor.ext.inject

fun Application.users() {

    val userService by inject<UserService>()

    routing {
        authenticate("auth-jwt") {
            route("/user") {
                post {
                    val email = call.principal<JWTPrincipal>()?.payload?.getClaim("email")?.asString()
                    if (email != null) {
                        val user = service.getUserByEmail(email)
                        call.respond(user?.copy(id = -1) ?: "Not Found")
                    }
                }
            }
        }
    }
}