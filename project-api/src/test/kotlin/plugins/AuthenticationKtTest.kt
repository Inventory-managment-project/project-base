package plugins

import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.testing.*
import mx.unam.fciencias.ids.eq1.model.user.CreateUserRequest
import mx.unam.fciencias.ids.eq1.plugins.authenticationRouting
import mx.unam.fciencias.ids.eq1.security.tokens.TokenProvider
import mx.unam.fciencias.ids.eq1.security.hashing.HashingService
import mx.unam.fciencias.ids.eq1.security.hashing.SHA256HashingService
import mx.unam.fciencias.ids.eq1.service.users.UserService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.mockito.kotlin.any

class AuthenticationKtTest {

    private lateinit var hashingService: HashingService
    private lateinit var userService: UserService
    private lateinit var tokenProvider: TokenProvider


    @BeforeEach
    fun setUp() {
        hashingService = SHA256HashingService()
        userService = mock(UserService::class.java)
        tokenProvider = mock(TokenProvider::class.java)
    }

    @Test
    fun `test register`() = testApplication {
        val mockUsers = listOf(
            CreateUserRequest( "John Doe", "john@example.com", "password123" ),
            CreateUserRequest( "Jane Smith", "no_valid_email", "password456" )
        )

        `when`(userService.addUser(any())).thenReturn(true)

        application {
            install(ContentNegotiation) {
                json()
            }
            authenticationRouting(
                hashingService,
                userService,
                tokenProvider
            )
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