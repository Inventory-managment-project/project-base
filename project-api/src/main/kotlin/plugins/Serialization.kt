package mx.unam.fciencias.ids.eq1.plugins

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.html.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import kotlinx.html.body
import kotlinx.html.h1
import kotlinx.html.head
import kotlinx.html.title
import mx.unam.fciencias.ids.eq1.service.UserService

fun Application.configureUsers(service: UserService) {
    install(ContentNegotiation) {
        json()
    }

    install(Authentication) {
        bearer("auth-bearer") {
            realm = "Access to the '/users' path"
            authenticate { tokenCredential ->
                if (tokenCredential.token == "abc123") {
                    UserIdPrincipal("jetbrains")
                } else {
                    null
                }
            }
        }
    }

    routing {
        authenticate("auth-bearer") {
            route("/users") {
                get {
                    val users = service.getAllUser()
                    call.respond(users)
                }
            }
            route("/hello") {
                get("/") {
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