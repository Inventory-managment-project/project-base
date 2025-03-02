package mx.unam.fciencias.ids.eq1.security.tokens

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import mx.unam.fciencias.ids.eq1.security.token.TokenClaim
import mx.unam.fciencias.ids.eq1.security.token.TokenConfig
import org.koin.core.annotation.Single
import java.time.Duration
import java.time.Instant

@Single
class JWTokenProvider : TokenProvider {
    override fun getToken(config: TokenConfig, vararg claims: TokenClaim): String {
        var token = JWT.create()
            .withAudience(config.audience)
            .withIssuer(config.issuer)
            .withExpiresAt(Instant.now()  + Duration.ofSeconds(config.expiresInSeconds))
        claims.forEach { token = token.withClaim(it.name, it.value) }
        return token.sign(Algorithm.HMAC256(config.secret))
    }
}