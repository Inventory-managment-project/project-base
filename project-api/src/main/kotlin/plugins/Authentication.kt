package mx.unam.fciencias.ids.eq1.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.http.auth.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*

/**
 * Configures JWT-based authentication for the application.
 * This function installs an authentication mechanism that uses JWT tokens
 * for securing endpoints and verifying user identities. The JWT token is
 * expected to be passed as a cookie named "token".
 *
 * @param environment The application's environment, used to fetch configuration values.
 */
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
                call.respond(HttpStatusCode.Unauthorized)
            }
        }
    }
}