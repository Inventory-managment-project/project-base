package mx.unam.fciencias.ids.eq1.routes.users

import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mx.unam.fciencias.ids.eq1.routes.getRequestEmailOrRespondBadRequest
import mx.unam.fciencias.ids.eq1.service.users.UserService
import org.koin.ktor.ext.inject

fun Route.users() {
    authenticate("auth-jwt") {
        route("/user") {
            post {
                val email = call.getRequestEmailOrRespondBadRequest() ?: return@post
                val userService by call.application.inject<UserService>()
                val user = userService.getUserByEmail(email)
                call.respond(user ?: "Not Found")
            }
        }
    }
}