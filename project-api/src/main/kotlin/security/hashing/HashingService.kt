package mx.unam.fciencias.ids.eq1.security.hashing

/**
 * Service interface for handling hashing-related operations.
 */
interface HashingService {

    /**
     * Generates a salted hash for a given value.
     *
     * @param value The value to hash.
     * @param saltLength The length of the generated salt (default is 32).
     * @return A [SaltedHash] containing the hashed value and the salt.
     */
    fun generateSaltedHash(value: String, saltLength: Int = 32): SaltedHash

    /**
     * Verifies if the given value matches the provided salted hash.
     *
     * @param value The value to verify.
     * @param saltedHash The previously generated [SaltedHash] to compare against.
     * @return `true` if the value matches the hash, otherwise `false`.
     */
    fun verifySaltedHash(value: String, saltedHash: SaltedHash): Boolean
}