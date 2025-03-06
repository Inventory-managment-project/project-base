package mx.unam.fciencias.ids.eq1.security.tokens

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import mx.unam.fciencias.ids.eq1.security.token.TokenClaim
import mx.unam.fciencias.ids.eq1.security.token.TokenConfig
import org.koin.core.annotation.Single
import java.time.Duration
import java.time.Instant

@Single
class JWTokenProvider(environment: ApplicationEnvironment) : TokenProvider {

    private val secret : String
    private val issuer : String
    private val audience : String
    private val myRealm : String

    init {
        secret = environment.config.property("jwt.secret").getString()
        issuer = environment.config.property("jwt.issuer").getString()
        audience = environment.config.property("jwt.audience").getString()
        myRealm = environment.config.property("jwt.realm").getString()

    }
    override fun getToken(config: TokenConfig, vararg claims: TokenClaim): String {
        var token = JWT.create()
            .withAudience(audience)
            .withIssuer(issuer)
            .withExpiresAt(Instant.now()  + Duration.ofSeconds(config.expiresInSeconds))
        claims.forEach { token = token.withClaim(it.name, it.value) }
        return token.sign(Algorithm.HMAC256(secret))
    }
}