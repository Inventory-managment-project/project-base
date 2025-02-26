package mx.unam.fciencias.ids.eq1.model.user

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id : Int,
    val name : String,
    val email : String,
    val password : String,
    val salt: String,
    val createdAt: String
)
