package mx.unam.fciencias.ids.eq1.model.user.repository

import mx.unam.fciencias.ids.eq1.model.user.User

/**
 * Repository interface defining operations for user data management.
 */
interface UserRepository {

    /**
     * Retrieves a user by their ID.
     */
    suspend fun getById(id: Int): User?

    /**
     * Retrieves a user by their name.
     */
    suspend fun getByName(name: String): User?

    /**
     * Retrieves all users.
     */
    suspend fun getAll(): List<User>

    /**
     * Retrieves users filtered by a custom condition.
     */
    suspend fun getFiltered(filter: (User) -> Boolean): List<User>

    /**
     * Deletes a user by ID.
     */
    suspend fun delete(id: Int): Boolean

    /**
     * Deletes all users.
     */
    suspend fun deleteAll(): Boolean

    /**
     * Counts the total number of users.
     */
    suspend fun count(): Long

    /**
     * Adds a new user.
     */
    suspend fun add(user: User): Boolean

    /**
     * Updates a user's email by their ID.
     */
    suspend fun updateEmail(userId: Int, email: String): Boolean
}