package mx.unam.fciencias.ids.eq1.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.html.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import kotlinx.html.*
import mx.unam.fciencias.ids.eq1.model.user.CreateUserRequest
import mx.unam.fciencias.ids.eq1.model.user.User
import mx.unam.fciencias.ids.eq1.security.request.AuthRequest
import mx.unam.fciencias.ids.eq1.security.tokens.TokenProvider
import mx.unam.fciencias.ids.eq1.security.hashing.HashingService
import mx.unam.fciencias.ids.eq1.security.hashing.SaltedHash
import mx.unam.fciencias.ids.eq1.security.token.TokenClaim
import mx.unam.fciencias.ids.eq1.security.token.TokenConfig
import mx.unam.fciencias.ids.eq1.service.users.UserService
import java.time.Instant



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

fun Application.authenticationRouting(
    hashingService: HashingService,
    userService: UserService,
    tokenProvider: TokenProvider
) {
    routing {
        route("login") {
            get {
                call.respondHtml {
                    body {
                        form(action = "/login", encType = FormEncType.applicationXWwwFormUrlEncoded, method = FormMethod.post) {
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


            post {
                val request = call.receive<AuthRequest>()
                val user = userService.getUserByEmail(request.username)
                if (user == null) {
                    call.respond(HttpStatusCode.Unauthorized, mapOf("message" to "Invalid credentials"))
                    return@post
                }
                val saltedHash = SaltedHash(user.hashedPassword, user.salt)
                if (hashingService.verifySaltedHash(request.password, saltedHash)) {
                    val tokenConfig = TokenConfig(
                        "http://localhost:8080/",
                        "users",
                        60000L,
                        "secret"
                    )
                    val claim = TokenClaim(
                        "user",
                        "true"
                    )
                    val email = TokenClaim(
                        "email",
                            user.email
                    )
                    val token = tokenProvider.getToken(tokenConfig, claim, email)
                    call.respond(HttpStatusCode.OK, mapOf(
                        "token" to token,
                        "tokenType" to "Bearer",
                    ))
                } else {
                    call.respond(HttpStatusCode.Unauthorized, mapOf("message" to "Invalid credentials"))
                }
            }
        }
        route("register") {
            post {
                val newUser = call.receive<CreateUserRequest>()
                val saltedHash = hashingService.generateSaltedHash(newUser.password)
                val user = User(
                    id = 0,
                    name = newUser.name,
                    email = newUser.email,
                    hashedPassword = saltedHash.hash,
                    salt = saltedHash.salt,
                    createdAt = Instant.now().epochSecond,
                )
                if (userService.addUser(user)) {
                    call.respond(HttpStatusCode.Created, mapOf("message" to "User created"))
                } else {
                    call.respond(HttpStatusCode.Conflict, mapOf("message" to "Error"))
                }

            }
        }

    }
}