package model.user

import kotlinx.coroutines.runBlocking
import mx.unam.fciencias.ids.eq1.db.user.UserTable
import mx.unam.fciencias.ids.eq1.model.user.repository.DBUserRepository
import mx.unam.fciencias.ids.eq1.model.user.User
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Instant
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class DBUserRepositoryTest {

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
    }

    @AfterEach
    fun tearDown() {
        transaction(database) {
            SchemaUtils.drop(UserTable)
        }
    }

    @Test
    fun `test add and retrieve user`() {
        runBlocking {
            val newUser = User(
                name = "Test User",
                email = "test@example.com",
                hashedPassword = "hashedpassword",
                salt = "randomsalt",
                createdAt = Instant.now().epochSecond,
            )

            val result = userRepository.add(newUser)
            assertTrue(result, "User should be added successfully")

            val allUsers = userRepository.getAll()
            assertEquals(1, allUsers.size, "Should have 1 user")

            val savedUser = allUsers[0]
            assertEquals("Test User", savedUser.name)
            assertEquals("test@example.com", savedUser.email)
            assertEquals("hashedpassword", savedUser.hashedPassword)
            assertEquals("randomsalt", savedUser.salt)
        }
    }


    @Test
    fun `test getById with existing and non-existing user`() {
        runBlocking {
            val user = User(
                id = 0,
                name = "Find Me",
                email = "find@example.com",
                hashedPassword = "password",
                salt = "salt123",
                createdAt = Instant.now().epochSecond,
            )
            userRepository.add(user)

            val userId = userRepository.getAll().first().id

            val foundUser = userRepository.getById(userId)
            assertNotNull(foundUser, "Should find existing user")
            assertEquals("Find Me", foundUser.name)

            val nonExistentUser = userRepository.getById(9999)
            assertNull(nonExistentUser, "Should return null for non-existent user")
        }
    }

    @Test
    fun `test getByName`() {
        runBlocking {
            userRepository.add(User(0, "Unique Name", "unique@example.com", "pass", "salt", Instant.now().epochSecond))
            userRepository.add(User(0, "Another Name", "another@example.com", "pass", "salt", Instant.now().epochSecond))

            val foundUser = userRepository.getByName("Unique Name")
            assertNotNull(foundUser, "Should find user by name")
            assertEquals("unique@example.com", foundUser.email)

            val nonExistentUser = userRepository.getByName("Does Not Exist")
            assertNull(nonExistentUser, "Should return null for non-existent name")
        }
    }

    @Test
    fun `test getFiltered`() {
        runBlocking {
            userRepository.add(User(0, "Admin User", "admin@example.com", "pass", "salt", Instant.now().epochSecond))
            userRepository.add(User(0, "Regular User", "user@example.com", "pass", "salt", Instant.now().epochSecond))
            userRepository.add(User(0, "Another Admin", "admin2@example.com", "pass", "salt", Instant.now().epochSecond))

            val adminUsers = userRepository.getFiltered { user -> user.email.contains("admin") }
            assertEquals(2, adminUsers.size, "Should find 2 admin users")

            val usersWithUserInName = userRepository.getFiltered { user -> user.name.contains("User") }
            assertEquals(2, usersWithUserInName.size, "Should find 2 users with 'User' in name")
        }
    }

    @Test
    fun `test updateEmail`() {
        runBlocking {
            userRepository.add(User(0, "Email Test", "old@example.com", "pass", "salt", Instant.now().epochSecond))
            val userId = userRepository.getAll().first().id

            val updateResult = userRepository.updateEmail(userId, "new@example.com")
            assertTrue(updateResult, "Email update should succeed")

            val updatedUser = userRepository.getById(userId)
            assertEquals("new@example.com", updatedUser!!.email, "Email should be updated")

            val failedUpdate = userRepository.updateEmail(9999, "nonexistent@example.com")
            assertFalse(failedUpdate, "Should return false when updating non-existent user")
        }
    }

    @Test
    fun `test delete user`() {
        runBlocking {
            userRepository.add(User(0, "To Delete", "delete@example.com", "pass", "salt", Instant.now().epochSecond))
            userRepository.add(User(0, "To Keep", "keep@example.com", "pass", "salt", Instant.now().epochSecond))

            val allUsers = userRepository.getAll()
            assertEquals(2, allUsers.size, "Should have 2 users initially")

            val userIdToDelete = allUsers.first { it.name == "To Delete" }

            val deleteResult = userRepository.delete(userIdToDelete.id)
            assertTrue(deleteResult, "Delete should succeed")

            assertEquals(1, userRepository.count(), "Should have 1 user after deletion")

            val remainingUser = userRepository.getAll().first()
            assertEquals("To Keep", remainingUser.name, "Correct user should remain")

            val failedDelete = userRepository.delete(9999)
            assertFalse(failedDelete, "Should return false when deleting non-existent user")
        }
    }

    @Test
    fun `test deleteAll`() {
        runBlocking {
            userRepository.add(User(0, "User 1", "user1@example.com", "pass", "salt", Instant.now().epochSecond))
            userRepository.add(User(0, "User 2", "user2@example.com", "pass", "salt", Instant.now().epochSecond))
            userRepository.add(User(0, "User 3", "user3@example.com", "pass", "salt", Instant.now().epochSecond))

            assertEquals(3, userRepository.count(), "Should have 3 users initially")

            val deleteResult = userRepository.deleteAll()
            assertTrue(deleteResult, "DeleteAll should succeed with users present")

            assertEquals(0, userRepository.count(), "Should have 0 users after deletion")
            assertTrue(userRepository.getAll().isEmpty(), "User list should be empty")

            val emptyDeleteResult = userRepository.deleteAll()
            assertFalse(emptyDeleteResult, "DeleteAll should return false when no users exist")
        }
    }

    @Test
    fun `test count`() {
        runBlocking {
            assertEquals(0, userRepository.count(), "Initial count should be 0")

            userRepository.add(User(0, "Count Test 1", "count1@example.com", "pass", "salt", Instant.now().epochSecond))
            assertEquals(1, userRepository.count(), "Count should be 1 after adding first user")

            userRepository.add(User(0, "Count Test 2", "count2@example.com", "pass", "salt", Instant.now().epochSecond))
            assertEquals(2, userRepository.count(), "Count should be 2 after adding second user")

            val userId = userRepository.getAll().first().id
            userRepository.delete(userId)
            assertEquals(1, userRepository.count(), "Count should be 1 after deleting a user")
        }
    }
}