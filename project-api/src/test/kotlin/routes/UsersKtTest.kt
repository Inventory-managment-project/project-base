package routes

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.json.Json
import mx.unam.fciencias.ids.eq1.model.user.User
import mx.unam.fciencias.ids.eq1.model.user.repository.UserRepository
import mx.unam.fciencias.ids.eq1.routes.users.configureUsers
import mx.unam.fciencias.ids.eq1.service.users.DBUserService
import mx.unam.fciencias.ids.eq1.service.users.UserService
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import java.time.Instant

class  UsersKtTest : KoinTest {

 private lateinit var mockUserRepository: UserRepository
 private lateinit var userService: UserService

 @BeforeEach
 fun setUp() {
  mockUserRepository = mock(UserRepository::class.java)
  userService = DBUserService(mockUserRepository)

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
   User(1, "John Doe", "john@example.com", "password123", "", Instant.now().epochSecond),
   User(2, "Jane Smith", "jane@example.com", "password456", "", Instant.now().epochSecond)
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