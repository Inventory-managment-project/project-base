package mx.unam.fciencias.ids.eq1.model.user

import kotlinx.serialization.Serializable

@Serializable
data class CreateUserRequest(
    val name: String,
    val email: String,
    val password: String
)

