package mx.unam.fciencias.ids.eq1.routes.users

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.html.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.html.body
import kotlinx.html.h1
import kotlinx.html.head
import kotlinx.html.title
import mx.unam.fciencias.ids.eq1.service.users.UserService

fun Application.configureUsers(service: UserService) {
    routing {
        authenticate("auth-jwt") {
            route("/user") {
                post {
                    val email = call.principal<JWTPrincipal>()?.payload?.getClaim("email")?.asString()
                    if (email != null) {
                        val user = service.getUserByEmail(email)
                        call.respond(user ?: "Not Found")
                    }
                }
            }
            route("/hello") {
                get {
                    val name = call.principal<UserIdPrincipal>()
                    call.respondHtml(HttpStatusCode.OK) {
                        head {
                            title {
                                +"Working"
                            }
                        }
                        body {
                            h1 {
                                +"Hello, $name!"
                            }
                        }
                    }
                }
            }
        }
    }
}