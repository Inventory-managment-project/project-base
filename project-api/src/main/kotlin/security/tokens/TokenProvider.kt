package mx.unam.fciencias.ids.eq1.security.tokens

import mx.unam.fciencias.ids.eq1.security.token.TokenClaim
import mx.unam.fciencias.ids.eq1.security.token.TokenConfig

interface TokenProvider {
     fun getToken(
         config : TokenConfig,
         vararg claims : TokenClaim
     ): String
}