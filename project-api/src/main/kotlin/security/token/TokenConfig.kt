package mx.unam.fciencias.ids.eq1.security.token

/**
 * Configuration for generating and validating JWT tokens.
 *
 * @param issuer The entity that issues the token.
 * @param audience The intended audience for the token.
 * @param expiresInSeconds The duration (in seconds) after which the token expires.
 * @param secret The secret key used to sign the token.
 */
data class TokenConfig(
    val issuer: String,
    val audience: String,
    val expiresInSeconds: Long,
    val secret: String
)
