package mx.unam.fciencias.ids.eq1.service.users

import mx.unam.fciencias.ids.eq1.model.user.User
import mx.unam.fciencias.ids.eq1.model.user.repository.UserRepository
import org.koin.core.annotation.Single

@Single
class DBUserService(private val userRepository: UserRepository) : UserService {

    override suspend fun getAllUser(): List<User> {
        return userRepository.getAll()
    }

    override suspend fun addUser(user: User): Boolean {
        return userRepository.add(user)
    }

    override suspend fun getUserByEmail(email: String): User? {
        return userRepository.getAll().firstOrNull { it.email == email }
    }

}