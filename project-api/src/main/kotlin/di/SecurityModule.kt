package mx.unam.fciencias.ids.eq1.di

import mx.unam.fciencias.ids.eq1.security.tokens.JWTokenProvider
import org.koin.dsl.module

class SecurityModule {
    val module = module {
        factory { JWTokenProvider(get()) }
    }
}