package routes

import io.ktor.client.call.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.testing.*
import mx.unam.fciencias.ids.eq1.db.user.UserTable
import mx.unam.fciencias.ids.eq1.model.user.CreateUserRequest
import mx.unam.fciencias.ids.eq1.model.user.User
import mx.unam.fciencias.ids.eq1.model.user.repository.DBUserRepository
import mx.unam.fciencias.ids.eq1.model.user.repository.UserRepository
import mx.unam.fciencias.ids.eq1.plugins.configureAuthentication
import mx.unam.fciencias.ids.eq1.routes.authentication.authenticationRouting
import mx.unam.fciencias.ids.eq1.routes.users.users
import mx.unam.fciencias.ids.eq1.security.hashing.HashingService
import mx.unam.fciencias.ids.eq1.security.hashing.SHA256HashingService
import mx.unam.fciencias.ids.eq1.security.request.AuthRequest
import mx.unam.fciencias.ids.eq1.security.tokenProvider.JWTokenProvider
import mx.unam.fciencias.ids.eq1.security.tokenProvider.TokenProvider
import mx.unam.fciencias.ids.eq1.service.users.DBUserService
import mx.unam.fciencias.ids.eq1.service.users.UserService
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.test.KoinTest
import kotlin.test.assertFails
import kotlin.test.assertNotEquals

class UsersKtTest : KoinTest {


    private lateinit var userService: UserService
    private lateinit var database: Database
    private lateinit var userRepository: DBUserRepository


    @BeforeEach
    fun setUp() {
        database = Database.connect(
            url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
            driver = "org.h2.Driver"
        )

        transaction(database) {
            SchemaUtils.create(UserTable)
        }

        userRepository = DBUserRepository(database)
        userService = DBUserService(userRepository)

        startKoin {
            modules(
                module {
                    single {
                        createTestEnvironment {
                            config = MapApplicationConfig(
                                "jwt.secret" to "secret",
                                "jwt.issuer" to "http://localhost:8080",
                                "jwt.audience" to "http://localhost:8080/login",
                                "jwt.realm" to "jwt.realm",
                            )
                        }
                    }
                    single { SHA256HashingService() } bind HashingService::class
                    single { userRepository } bind UserRepository::class
                    single { userService } bind UserService::class
                    single { JWTokenProvider(get()) } bind TokenProvider::class
                }
            )
        }
    }

    @AfterEach
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun testGetOwnUser() = testApplication {
        environment {
            config = MapApplicationConfig(
                "jwt.secret" to "secret",
                "jwt.issuer" to "http://localhost:8080",
                "jwt.audience" to "http://localhost:8080/login",
                "jwt.realm" to "jwt.realm",
            )
        }

        application {
            install(ContentNegotiation) {
                json()
            }
            configureAuthentication(environment)
            authenticationRouting(environment)
            users()
        }


        val client = createClient {
            install(io.ktor.client.plugins.contentnegotiation.ContentNegotiation) {
                json()
            }
            install(HttpCookies) {
            }
        }

        var response = client.post("/user")
        assertNotEquals(HttpStatusCode.OK, response.status)

        val name = "testUser"
        val email = "test@test.com"
        val password = "test"
        val createdUser = CreateUserRequest(name, email, password)
        response = client.post("/register") {
            contentType(ContentType.Application.Json)
            setBody(createdUser)
        }
        assertEquals(HttpStatusCode.Created, response.status)

        val badLoginRequest = AuthRequest(email, password + "bad")
        response = client.post("/login") {
            contentType(ContentType.Application.Json)
            setBody(badLoginRequest)
        }
        assertEquals(HttpStatusCode.Unauthorized, response.status)

        val logInRequest = AuthRequest(email, password)
        response = client.post("/login") {
            contentType(ContentType.Application.Json)
            setBody(logInRequest)
        }
        assertEquals(HttpStatusCode.OK, response.status)

        val token = response.call.body<Map<String,String>>().getOrElse("token") {
            return@getOrElse assertFails("No Auth Token found") {}
        }.toString()

        response = client.post("/user") {
            bearerAuth(token)
        }
        assertEquals(HttpStatusCode.OK, response.status)
        val userResponse = response.body<User>()
        assertEquals("testUser", userResponse.name)
        assertEquals("test@test.com", userResponse.email)
    }
}