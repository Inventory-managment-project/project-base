import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.json.Json
import mx.unam.fciencias.ids.eq1.model.user.User
import mx.unam.fciencias.ids.eq1.model.user.UserRepository
import mx.unam.fciencias.ids.eq1.plugins.configureUsers
import mx.unam.fciencias.ids.eq1.service.UserService
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.koin.test.KoinTest
import org.junit.jupiter.api.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import kotlin.test.assertEquals


class UserApiTest : KoinTest {

    private lateinit var mockUserRepository: UserRepository
    private lateinit var userService: UserService

    @BeforeEach
    fun setUp() {
        mockUserRepository = mock(UserRepository::class.java)
        userService = UserService(mockUserRepository)

        startKoin {
            modules(module {
                single { mockUserRepository }
                single { userService }
            })
        }
    }

    @AfterEach
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun testGetAllUsers() = testApplication {
        val mockUsers = listOf(
            User(1, "John Doe", "john@example.com", "password123", "", ""),
            User(2, "Jane Smith", "jane@example.com", "password456", "", "")
        )

        `when`(mockUserRepository.getAll()).thenReturn(mockUsers)

        application {
            configureUsers(userService)
        }

        val response = client.get("/users")

        assertEquals(HttpStatusCode.OK, response.status)

        val responseBody = response.bodyAsText()
        val users = Json.decodeFromString<List<User>>(responseBody)

        assertEquals(2, users.size)
        assertEquals(1, users[0].id)
        assertEquals("John Doe", users[0].name)
        assertEquals("john@example.com", users[0].email)
        assertEquals(2, users[1].id)
        assertEquals("Jane Smith", users[1].name)
        assertEquals("jane@example.com", users[1].email)
    }

    @Test
    fun testGetAllUsersEmpty() = testApplication {
        `when`(mockUserRepository.getAll()).thenReturn(emptyList())

        application {
            configureUsers(userService)
        }

        val response = client.get("/users")

        assertEquals(HttpStatusCode.OK, response.status)

        val responseBody = response.bodyAsText()
        val users = Json.decodeFromString<List<User>>(responseBody)

        assertEquals(0, users.size)
    }
}