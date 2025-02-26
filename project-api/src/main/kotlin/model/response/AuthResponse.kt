package mx.unam.fciencias.ids.eq1.response

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val token: String
)