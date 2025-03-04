package mx.unam.fciencias.ids.eq1.security.hashing

data class SaltedHash(
    val hash: String,
    val salt: String
)
