package mx.unam.fciencias.ids.eq1.security.token

data class TokenConfig(
    val issuer: String,
    val audience: String,
    val expiresInSeconds: Long,
    val secret: String
)
