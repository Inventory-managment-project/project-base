package model.user

import kotlinx.coroutines.runBlocking
import mx.unam.fciencias.ids.eq1.db.user.UserTable
import mx.unam.fciencias.ids.eq1.model.user.DBUserRepository
import mx.unam.fciencias.ids.eq1.model.user.User
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
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
    id = 0, // Will be assigned by DB
    name = "Test User",
    email = "test@example.com",
    password = "hashedpassword",
    salt = "randomsalt",
    createdAt = "2023-01-01T12:00:00"
   )

   // Add user
   val result = userRepository.add(newUser)
   assertTrue(result, "User should be added successfully")

   // Get all users - should contain our new user
   val allUsers = userRepository.getAll()
   assertEquals(1, allUsers.size, "Should have 1 user")

   // Verify user data
   val savedUser = allUsers[0]
   assertEquals("Test User", savedUser.name)
   assertEquals("test@example.com", savedUser.email)
   assertEquals("hashedpassword", savedUser.password)
   assertEquals("randomsalt", savedUser.salt)
   assertEquals("2023-01-01T12:00:00", savedUser.createdAt)
  }
 }


 @Test
 fun `test getById with existing and non-existing user`() {
  // Add a test user
  runBlocking {
  val user = User(
   id = 0,
   name = "Find Me",
   email = "find@example.com",
   password = "password",
   salt = "salt123",
   createdAt = "2023-01-01T12:00:00"
  )
  userRepository.add(user)

  // Get the user ID
  val userId = userRepository.getAll().first().id

  // Test getting existing user
  val foundUser = userRepository.getById(userId)
  assertNotNull(foundUser, "Should find existing user")
  assertEquals("Find Me", foundUser.name)

  // Test getting non-existing user
  val nonExistentUser = userRepository.getById(9999)
  assertNull(nonExistentUser, "Should return null for non-existent user")
  }
 }

 @Test
 fun `test getByName`() {
  // Add users with different names
  runBlocking {
  userRepository.add(User(0, "Unique Name", "unique@example.com", "pass", "salt", "2023-01-01T12:00:00"))
  userRepository.add(User(0, "Another Name", "another@example.com", "pass", "salt", "2023-01-01T12:00:00"))

  // Find by name
  val foundUser = userRepository.getByName("Unique Name")
  assertNotNull(foundUser, "Should find user by name")
  assertEquals("unique@example.com", foundUser.email)

  // Test non-existent name
  val nonExistentUser = userRepository.getByName("Does Not Exist")
  assertNull(nonExistentUser, "Should return null for non-existent name")
  }
 }

 @Test
 fun `test getFiltered`() {
  // Add multiple users
  runBlocking {
  userRepository.add(User(0, "Admin User", "admin@example.com", "pass", "salt", "2023-01-01T12:00:00"))
  userRepository.add(User(0, "Regular User", "user@example.com", "pass", "salt", "2023-01-01T12:00:00"))
  userRepository.add(User(0, "Another Admin", "admin2@example.com", "pass", "salt", "2023-01-01T12:00:00"))

  // Filter admins based on email
  val adminUsers = userRepository.getFiltered { user -> user.email.contains("admin") }
  assertEquals(2, adminUsers.size, "Should find 2 admin users")

  // Filter by name containing "User"
  val usersWithUserInName = userRepository.getFiltered { user -> user.name.contains("User") }
  assertEquals(2, usersWithUserInName.size, "Should find 2 users with 'User' in name")
  }
 }

 @Test
 fun `test updateEmail`() {
  // Add a user
  runBlocking {
   userRepository.add(User(0, "Email Test", "old@example.com", "pass", "salt", "2023-01-01T12:00:00"))
   val userId = userRepository.getAll().first().id

   // Update email
   val updateResult = userRepository.updateEmail(userId, "new@example.com")
   assertTrue(updateResult, "Email update should succeed")

   // Verify email was updated
   val updatedUser = userRepository.getById(userId)
   assertEquals("new@example.com", updatedUser!!.email, "Email should be updated")

   // Test updating non-existent user
   val failedUpdate = userRepository.updateEmail(9999, "nonexistent@example.com")
   assertFalse(failedUpdate, "Should return false when updating non-existent user")
  }
 }

 @Test
 fun `test delete user`() {
  // Add users
  runBlocking {
   userRepository.add(User(0, "To Delete", "delete@example.com", "pass", "salt", "2023-01-01T12:00:00"))
   userRepository.add(User(0, "To Keep", "keep@example.com", "pass", "salt", "2023-01-01T12:00:00"))

   val allUsers = userRepository.getAll()
   assertEquals(2, allUsers.size, "Should have 2 users initially")

   // Get ID of first user
   val userIdToDelete = allUsers.first { it.name == "To Delete" }.id

   // Delete user
   val deleteResult = userRepository.delete(userIdToDelete)
   assertTrue(deleteResult, "Delete should succeed")

   // Verify user count
   assertEquals(1, userRepository.count(), "Should have 1 user after deletion")

   // Verify correct user remains
   val remainingUser = userRepository.getAll().first()
   assertEquals("To Keep", remainingUser.name, "Correct user should remain")

   // Test deleting non-existent user
   val failedDelete = userRepository.delete(9999)
   assertFalse(failedDelete, "Should return false when deleting non-existent user")
  }
 }

 @Test
 fun `test deleteAll`() {
  // Add multiple users
  runBlocking {
  userRepository.add(User(0, "User 1", "user1@example.com", "pass", "salt", "2023-01-01T12:00:00"))
  userRepository.add(User(0, "User 2", "user2@example.com", "pass", "salt", "2023-01-01T12:00:00"))
  userRepository.add(User(0, "User 3", "user3@example.com", "pass", "salt", "2023-01-01T12:00:00"))

  assertEquals(3, userRepository.count(), "Should have 3 users initially")

  // Delete all users
  val deleteResult = userRepository.deleteAll()
  assertTrue(deleteResult, "DeleteAll should succeed with users present")

  // Verify all users are gone
  assertEquals(0, userRepository.count(), "Should have 0 users after deletion")
  assertTrue(userRepository.getAll().isEmpty(), "User list should be empty")

  // Test deleteAll on empty repository
  val emptyDeleteResult = userRepository.deleteAll()
  assertFalse(emptyDeleteResult, "DeleteAll should return false when no users exist")
  }
 }

 @Test
 fun `test count`() {
  runBlocking {
  assertEquals(0, userRepository.count(), "Initial count should be 0")

  // Add users
  userRepository.add(User(0, "Count Test 1", "count1@example.com", "pass", "salt", "2023-01-01T12:00:00"))
  assertEquals(1, userRepository.count(), "Count should be 1 after adding first user")

  userRepository.add(User(0, "Count Test 2", "count2@example.com", "pass", "salt", "2023-01-01T12:00:00"))
  assertEquals(2, userRepository.count(), "Count should be 2 after adding second user")

  // Delete a user
  val userId = userRepository.getAll().first().id
  userRepository.delete(userId)
  assertEquals(1, userRepository.count(), "Count should be 1 after deleting a user")
  }
 }
}