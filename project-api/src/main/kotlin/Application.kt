package mx.unam.fciencias.ids.eq1

import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.http.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import mx.unam.fciencias.ids.eq1.di.*
import mx.unam.fciencias.ids.eq1.plugins.configureAuthentication
import mx.unam.fciencias.ids.eq1.plugins.configureSerialization
import mx.unam.fciencias.ids.eq1.routes.authentication.authenticationRouting
import mx.unam.fciencias.ids.eq1.routes.store.createStores
import mx.unam.fciencias.ids.eq1.routes.store.storeRoutes
import mx.unam.fciencias.ids.eq1.routes.users.users
import org.koin.ksp.generated.module
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
            StoreModule().module,
            DatabaseModule().module,
            SecurityModule().module,
            AppModule(environment).module,
            ProductModule().module,
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

    install(CORS) {
        anyHost() 
        allowCredentials = true
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.Authorization)
        allowMethod(HttpMethod.Options) 
        allowMethod(HttpMethod.Post) 
    }


    //Plugins
    configureAuthentication(environment)
    configureSerialization()

    //Routes
    authenticationRouting(environment)
    users()
    createStores()
    routing {
        storeRoutes()
    }
}
