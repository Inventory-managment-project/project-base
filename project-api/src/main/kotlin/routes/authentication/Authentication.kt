package mx.unam.fciencias.ids.eq1.routes.authentication

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mx.unam.fciencias.ids.eq1.model.user.CreateUserRequest
import mx.unam.fciencias.ids.eq1.model.user.User
import mx.unam.fciencias.ids.eq1.security.hashing.HashingService
import mx.unam.fciencias.ids.eq1.security.hashing.SaltedHash
import mx.unam.fciencias.ids.eq1.security.request.AuthRequest
import mx.unam.fciencias.ids.eq1.security.token.TokenClaim
import mx.unam.fciencias.ids.eq1.security.token.TokenConfig
import mx.unam.fciencias.ids.eq1.security.tokens.TokenProvider
import mx.unam.fciencias.ids.eq1.service.users.UserService
import org.koin.ktor.ext.inject
import java.time.Instant

fun Application.authenticationRouting() {

    val hashingService by inject<HashingService>()
    val userService by inject<UserService>()
    val tokenProvider by inject<TokenProvider>()
    val environment by inject<ApplicationEnvironment>()

    routing {
        route("login") {
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
                        environment.config.property("jwt.issuer").getString(),
                        environment.config.property("jwt.audience").getString(),
                        60000L,
                        environment.config.property("jwt.secret").getString()
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
                    call.response.cookies.append(
                        name = "token",
                        value = token,
                        encoding = CookieEncoding.BASE64_ENCODING
                    )
                    call.respond(HttpStatusCode.OK, mapOf("token" to token))
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
        authenticate("auth-jwt") {
            route("validate") {
                post {
                    call.respond(HttpStatusCode.OK, mapOf("message" to "Token is valid"))
                }
            }
        }
    }
}