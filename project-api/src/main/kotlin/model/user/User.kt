package mx.unam.fciencias.ids.eq1.model.user

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id : Int = -1,
    val name: String,
    val email: String,
    val hashedPassword: String,
    val salt: String,
    val createdAt: Long
)
