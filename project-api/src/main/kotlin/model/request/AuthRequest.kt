package mx.unam.fciencias.ids.eq1.model.request

import kotlinx.serialization.Serializable

@Serializable
data class AuthRequest(
    val username: String,
    val password: String
)
