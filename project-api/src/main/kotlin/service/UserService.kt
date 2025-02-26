package mx.unam.fciencias.ids.eq1.service

import mx.unam.fciencias.ids.eq1.model.user.User
import mx.unam.fciencias.ids.eq1.model.user.UserRepository
import org.koin.core.annotation.Single

@Single
class UserService(private val userRepository: UserRepository) {

    fun getAllUser(): List<User> {
        return userRepository.getAll()
    }

}