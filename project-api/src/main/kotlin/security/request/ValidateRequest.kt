package mx.unam.fciencias.ids.eq1.security.request

import kotlinx.serialization.Serializable

@Serializable
data class ValidateRequest(
    val token: String
)
