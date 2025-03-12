package plugins

import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.testing.*
import mx.unam.fciencias.ids.eq1.model.user.CreateUserRequest
import mx.unam.fciencias.ids.eq1.plugins.configureAuthentication
import mx.unam.fciencias.ids.eq1.routes.authentication.authenticationRouting
import mx.unam.fciencias.ids.eq1.security.hashing.HashingService
import mx.unam.fciencias.ids.eq1.security.hashing.SHA256HashingService
import mx.unam.fciencias.ids.eq1.security.tokenProvider.JWTokenProvider
import mx.unam.fciencias.ids.eq1.service.users.UserService
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.bind
import org.koin.dsl.module
import org.mockito.Mockito.*
import org.mockito.kotlin.any

class AuthenticationKtTest {

    private lateinit var userService: UserService

    @BeforeEach
    fun setUp() {
        userService = mock(UserService::class.java)

        startKoin {
            modules(
                module {
                    single { SHA256HashingService() } bind HashingService::class
                    single { userService }
                    single { JWTokenProvider(get()) }
                }
            )
        }
    }

    @AfterEach
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `test register`() = testApplication {
        environment {
            config = MapApplicationConfig(
                "jwt.secret" to "secret",
                "jwt.issuer" to "http://localhost:8080",
                "jwt.audience" to "http://localhost:8080/login",
                "jwt.realm" to "jwt.realm",
            )
        }
        val mockUsers = listOf(
            CreateUserRequest("John Doe", "john@example.com", "password123"),
            CreateUserRequest("Jane Smith", "no_valid_email", "password456")
        )

        `when`(userService.addUser(any())).thenReturn(true)

        application {
            install(ContentNegotiation) {
                json()
            }
            configureAuthentication(environment)
            authenticationRouting(environment)
        }

        val client = createClient {
            install(io.ktor.client.plugins.contentnegotiation.ContentNegotiation) {
                json()
            }
        }

        var response = client.post("/register") {
            contentType(ContentType.Application.Json)
            setBody(mockUsers[0])
        }
        assertEquals(HttpStatusCode.Created, response.status)
        response = client.post("/register") {
            contentType(ContentType.Application.Json)
            setBody(mockUsers[1])
        }
        assertEquals(HttpStatusCode.Conflict, response.status)
    }
}