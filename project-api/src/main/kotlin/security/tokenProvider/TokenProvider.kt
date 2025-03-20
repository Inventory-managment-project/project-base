package mx.unam.fciencias.ids.eq1.security.tokenProvider

import mx.unam.fciencias.ids.eq1.security.token.TokenClaim
import mx.unam.fciencias.ids.eq1.security.token.TokenConfig

/**
 * Provides functionality to generate JWT tokens with custom claims.
 */
interface TokenProvider {

    /**
     * Generates a JWT token with the specified configuration and claims.
     *
     * @param config The [TokenConfig] including issuer, audience, expiration, etc.
     * @param claims Variable number of [TokenClaim] to include in the token payload.
     * @return A signed JWT token as a string.
     */
    fun getToken(
        config: TokenConfig,
        vararg claims: TokenClaim
    ): String

    /**
     * Generates a JWT token with the specified configuration and claims.
     *
     * @param claims Variable number of [TokenClaim] to include in the token payload.
     * @return A signed JWT token as a string.
     */
    fun getToken( vararg claims: TokenClaim): String
}