package mx.unam.fciencias.ids.eq1.service.users

import mx.unam.fciencias.ids.eq1.model.user.User

/**
 * Interface for user-related operations.
 */
interface UserService {
    suspend fun getAllUser(): List<User>
    suspend fun addUser(user: User): Boolean
    suspend fun getUserByEmail(email: String): User?
}