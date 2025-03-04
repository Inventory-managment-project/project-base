package mx.unam.fciencias.ids.eq1

import io.ktor.server.application.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.request.*
import mx.unam.fciencias.ids.eq1.di.DatabaseModule
import mx.unam.fciencias.ids.eq1.di.UserModule
import mx.unam.fciencias.ids.eq1.plugins.configureAuthentication
import mx.unam.fciencias.ids.eq1.plugins.configureSerialization
import mx.unam.fciencias.ids.eq1.routes.authentication.authenticationRouting
import mx.unam.fciencias.ids.eq1.routes.users.configureUsers
import mx.unam.fciencias.ids.eq1.security.tokens.TokenProvider
import mx.unam.fciencias.ids.eq1.security.hashing.HashingService
import mx.unam.fciencias.ids.eq1.service.users.UserService
import org.koin.ksp.generated.module
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import org.slf4j.event.Level


fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    install(Koin) {
        slf4jLogger()
        modules(
            UserModule().module,
            DatabaseModule().module,
        )
    }

    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/") }
        format { call ->
            val status = call.response.status()
            val httpMethod = call.request.httpMethod.value
            val userAgent = call.request.headers["User-Agent"]
            val authHeader = call.request.headers["Authorization"] ?: "No Auth Header"
            "Status: $status, HTTP method: $httpMethod, User agent: $userAgent, Auth: $authHeader "
        }
    }

    val userService by inject<UserService>()
    val tokenProvider by inject<TokenProvider>()
    val hashingService by inject<HashingService>()

    configureAuthentication("secret")
    authenticationRouting(hashingService, userService, tokenProvider)
    configureUsers(userService)
    configureSerialization()
}
