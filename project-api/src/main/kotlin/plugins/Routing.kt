package mx.unam.fciencias.ids.eq1.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.routing.*
import kotlinx.html.*

fun Application.configureRouting() {
    routing {
        get("/") {
            val name = "Working"
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
        get("/login") {
            call.respondHtml(HttpStatusCode.OK) {
                body {
                    form(
                        action = "/login",
                        encType = FormEncType.applicationXWwwFormUrlEncoded,
                        method = FormMethod.post
                    ) {
                        p {
                            +"Username:"
                            textInput(name = "username")
                        }
                        p {
                            +"Password:"
                            passwordInput(name = "password")
                        }
                        p {
                            submitInput { value = "Login" }
                        }
                    }
                }
            }
        }
    }
}
