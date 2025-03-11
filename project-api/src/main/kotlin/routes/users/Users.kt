package mx.unam.fciencias.ids.eq1.routes.users

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.html.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.html.*
import mx.unam.fciencias.ids.eq1.service.users.UserService

fun Application.configureUsers(service: UserService) {
    routing {
        authenticate("auth-jwt") {
            route("/viewMyUser") {
                get {
                    val email = call.principal<JWTPrincipal>()?.payload?.getClaim("email")?.asString()
                    if (email == null) {
                        call.respond(HttpStatusCode.BadRequest)
                    }
                    val user = service.getUserByEmail(email!!)
                    if (user != null) {
                        call.respondHtml {
                            head {
                                title { +"My User" }
                            }
                            body {
                                h1 {
                                    + "Hi My User is ${user.name}"
                                }
                                h2 {
                                    + "Email is ${user.email}"
                                }
                            }
                        }
                    } else {
                        call.respond(HttpStatusCode.BadRequest)
                    }
                }
            }
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