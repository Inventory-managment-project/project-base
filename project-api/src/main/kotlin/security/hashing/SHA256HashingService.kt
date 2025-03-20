package mx.unam.fciencias.ids.eq1.security.hashing

import org.apache.commons.codec.binary.Hex
import org.apache.commons.codec.digest.DigestUtils
import org.koin.core.annotation.Single
import java.security.SecureRandom

/**
 * Provides SHA-256 hashing functionality with salted hashes.
 */
@Single
class SHA256HashingService : HashingService {
    override fun generateSaltedHash(value: String, saltLength: Int): SaltedHash {
        val salt = SecureRandom.getInstance("SHA1PRNG").generateSeed(saltLength)
        val saltHex = Hex.encodeHexString(salt)
        val hash = DigestUtils.sha256Hex(value + saltHex)
        return SaltedHash(hash, saltHex)
    }
    override fun verifySaltedHash(value: String, saltedHash: SaltedHash): Boolean =
        if (value.isBlank()) false
        else DigestUtils.sha256Hex(value + saltedHash.salt) == saltedHash.hash
}