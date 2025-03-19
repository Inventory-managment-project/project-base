package mx.unam.fciencias.ids.eq1.security.request

import kotlinx.serialization.Serializable

/**
 * Represents an authentication request containing user credentials.
 *
 * @property username The username or email of the user.
 * @property password The password of the user.
 */
@Serializable
data class AuthRequest(
    val username: String,
    val password: String
)
