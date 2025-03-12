package mx.unam.fciencias.ids.eq1.plugins

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*

/**
 * Configures content serialization for the application using JSON format.
 * Installs the ContentNegotiation feature and sets up JSON serialization.
 */

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json()
    }
}

