package mx.unam.fciencias.ids.eq1

import io.ktor.server.application.*
import mx.unam.fciencias.ids.eq1.di.DatabaseModule
import mx.unam.fciencias.ids.eq1.di.UserModule
import mx.unam.fciencias.ids.eq1.plugins.authenticationRouting
import mx.unam.fciencias.ids.eq1.plugins.configureAuthentication
import mx.unam.fciencias.ids.eq1.plugins.configureSerialization
import mx.unam.fciencias.ids.eq1.routes.configureUsers
import mx.unam.fciencias.ids.eq1.security.tokens.TokenProvider
import mx.unam.fciencias.ids.eq1.security.hashing.HashingService
import mx.unam.fciencias.ids.eq1.service.users.DBUserService
import org.koin.ksp.generated.module
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger


fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    install(Koin) {
        slf4jLogger()
        modules(
            UserModule().module,
            DatabaseModule().module
        )
    }

    val userService by inject<DBUserService>()
    val tokenProvider by inject<TokenProvider>()
    val hashingService by inject<HashingService>()

    configureAuthentication("secret")
    authenticationRouting(hashingService, userService, tokenProvider)
    configureUsers(userService)
    configureSerialization()
}
