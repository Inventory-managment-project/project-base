package mx.unam.fciencias.ids.eq1.service.users

import mx.unam.fciencias.ids.eq1.model.user.User
import mx.unam.fciencias.ids.eq1.model.user.repository.UserRepository
import org.koin.core.annotation.Single

/**
 * Service implementation for managing user data.
 */
@Single
class DBUserService(private val userRepository: UserRepository) : UserService {

    /**
     * Retrieves all users.
     *
     * @return A list of [User] objects.
     */
    override suspend fun getAllUser(): List<User> {
        return userRepository.getAll()
    }

    /**
     * Adds a new user.
     *
     * @param user The [User] object to add.
     * @return `true` if the user was added successfully, `false` otherwise.
     */
    override suspend fun addUser(user: User): Boolean {
        return userRepository.add(user)
    }

    /**
     * Finds a user by their email address.
     *
     * @param email The email address to search for.
     * @return The matching [User] object if found, `null` otherwise.
     */
    override suspend fun getUserByEmail(email: String): User? {
        return userRepository.getAll().firstOrNull { it.email == email }
    }
}