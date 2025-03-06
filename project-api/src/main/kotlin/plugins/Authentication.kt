package mx.unam.fciencias.ids.eq1.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.http.auth.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*

fun Application.configureAuthentication(environment: ApplicationEnvironment) {
    install(Authentication) {
        jwt("auth-jwt") {
            realm = "User JWT Auth"
            authHeader { call ->
                val token = call.request.cookies["token", CookieEncoding.BASE64_ENCODING]
                if(token.isNullOrBlank()) {
                    return@authHeader null
                } else {
                    HttpAuthHeader.Single("Bearer", token)
                }
            }
            verifier(
                JWT.require(Algorithm.HMAC256(environment.config.property("jwt.secret").getString()))
                    .withIssuer(environment.config.property("jwt.issuer").getString())
                    .withAudience(environment.config.property("jwt.audience").getString())
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