package mx.unam.fciencias.ids.eq1

import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.http.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import mx.unam.fciencias.ids.eq1.db.store.StoreTable
import mx.unam.fciencias.ids.eq1.db.store.product.ProductTable
import mx.unam.fciencias.ids.eq1.db.user.UserTable
import mx.unam.fciencias.ids.eq1.di.*
import mx.unam.fciencias.ids.eq1.plugins.configureAuthentication
import mx.unam.fciencias.ids.eq1.plugins.configureSerialization
import mx.unam.fciencias.ids.eq1.routes.authentication.authenticationRouting
import mx.unam.fciencias.ids.eq1.routes.stores.storeRoutes
import mx.unam.fciencias.ids.eq1.routes.users.users
import routes.retailAnalyticsRoutes
import service.RetailAnalyticsService
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.ksp.generated.module
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import org.slf4j.event.Level
import io.ktor.server.plugins.statuspages.*
import io.ktor.serialization.*
import io.ktor.server.response.*
import kotlinx.serialization.SerializationException
import io.ktor.server.plugins.contentnegotiation.*


fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.configureExceptionHandling() {
    install(StatusPages) {
        exception<ContentTransformationException> { call, cause ->
            call.application.environment.log.error("ContentTransformationException", cause)
            call.respond(
                HttpStatusCode.BadRequest,
                mapOf("error" to "Failed to parse body: ${cause.message ?: "Unknown error"}")
            )
        }
        exception<SerializationException> { call, cause ->
            call.application.environment.log.error("SerializationException", cause)
            call.respond(
                HttpStatusCode.BadRequest,
                mapOf("error" to "Serialization error: ${cause.message ?: "Unknown error"}")
            )
        }
        exception<Throwable> { call, cause ->
            call.application.environment.log.error("Unhandled exception", cause)
            call.respond(
                HttpStatusCode.InternalServerError,
                mapOf("error" to "Unexpected error: ${cause.message ?: "Unknown error"}")
            )
        }
    }
}

fun Application.module() {
    configureExceptionHandling()
    install(Koin) {
        slf4jLogger()
        modules(
            UserModule().module,
            StoreModule().module,
            DatabaseModule().module,
            AppModule(environment).module,
            SalesModule().module,
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
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
    }

    val database by inject<Database>()
    transaction(database) {
        SchemaUtils.create(
            UserTable,
            StoreTable,
            ProductTable
        )
    }

    //Plugins
    configureAuthentication(environment)
    configureSerialization()

    //Routes
    val retailAnalyticsService = RetailAnalyticsService()

    routing {
        users()
        storeRoutes()
        authenticationRouting(environment)
        retailAnalyticsRoutes(retailAnalyticsService)
    }
}
