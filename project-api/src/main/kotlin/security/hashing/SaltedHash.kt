package mx.unam.fciencias.ids.eq1.security.hashing


/**
 * Represents a hashed value along with its corresponding salt for verification.
 *
 * @param hash The resulting hashed value.
 * @param salt The salt used to create the hash.
 */
data class SaltedHash(
    val hash: String,
    val salt: String
)