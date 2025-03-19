package mx.unam.fciencias.ids.eq1.security.token

/**
 * Represents a claim to be included in a JWT token.
 *
 * @property name The name of the claim.
 * @property value The value of the claim.
 */
data class TokenClaim(
    val name: String,
    val value: String
)
