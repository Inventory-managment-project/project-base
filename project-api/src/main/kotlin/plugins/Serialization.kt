package mx.unam.fciencias.ids.eq1.plugins

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import mx.unam.fciencias.ids.eq1.service.UserService

fun Application.configureSerialization(repository: UserService) {
    install(ContentNegotiation) {
        json()
    }
    routing {
        route("/users") {
            get {
                val users = repository.getAllUser()
                call.respond(users)
            }
        }
    }
}