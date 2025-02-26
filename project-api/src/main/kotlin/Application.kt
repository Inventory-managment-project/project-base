package mx.unam.fciencias.ids.eq1

import io.ktor.server.application.*
import mx.unam.fciencias.ids.eq1.di.UserModule
import mx.unam.fciencias.ids.eq1.plugins.configureRouting
import mx.unam.fciencias.ids.eq1.plugins.configureSerialization
import mx.unam.fciencias.ids.eq1.service.UserService
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
        modules(UserModule().module)
    }

    val userService by inject<UserService>()

    configureSerialization(userService)
    configureRouting()
}
