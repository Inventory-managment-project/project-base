package mx.unam.fciencias.ids.eq1.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*

fun Application.configureAuthentication(secret: String) {
    install(Authentication) {
        jwt("auth-jwt") {
            realm = "User JWT Auth"
            verifier(
                JWT.require(Algorithm.HMAC256(secret))
                    .withIssuer("http://localhost:8080/")
                    .withAudience("users")
                    .build()
            )
            validate { credential ->
                if (credential.payload.getClaim("user").asString() == "true") {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
            challenge { _, _ ->
                call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired")
            }
        }
    }
}