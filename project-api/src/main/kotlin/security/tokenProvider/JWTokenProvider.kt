package mx.unam.fciencias.ids.eq1.security.tokenProvider

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import mx.unam.fciencias.ids.eq1.security.token.TokenClaim
import mx.unam.fciencias.ids.eq1.security.token.TokenConfig
import org.koin.core.annotation.Single
import java.time.Duration
import java.time.Instant

/**
 * Implementation of [TokenProvider] that generates JWT tokens using environment configuration.
 *
 * @property secret The secret key for signing the token.
 * @property issuer The issuer of the token.
 * @property audience The intended audience of the token.
 * @property myRealm The realm associated with the token.
 */
@Single
class JWTokenProvider(environment: ApplicationEnvironment) : TokenProvider {

    private val secret = environment.config.property("jwt.secret").getString()
    private val issuer: String = environment.config.property("jwt.issuer").getString()
    private val audience: String = environment.config.property("jwt.audience").getString()
    private val myRealm: String  = environment.config.property("jwt.realm").getString()

    override fun getToken(config: TokenConfig, vararg claims: TokenClaim): String {
        var token = JWT.create()
            .withAudience(config.audience)
            .withIssuer(config.issuer)
            .withExpiresAt(Instant.now() + Duration.ofSeconds(config.expiresInSeconds))
        claims.forEach { token = token.withClaim(it.name, it.value) }
        return token.sign(Algorithm.HMAC256(secret))
    }
    override fun getToken( vararg claims: TokenClaim): String {
        var token = JWT.create()
            .withAudience(audience)
            .withIssuer(issuer)
            .withExpiresAt(Instant.now() + Duration.ofSeconds(3600))
        claims.forEach { token = token.withClaim(it.name, it.value) }
        return token.sign(Algorithm.HMAC256(secret))
    }
}