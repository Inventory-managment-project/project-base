package mx.unam.fciencias.ids.eq1.di

import io.ktor.server.application.*
import org.koin.dsl.module

class AppModule(environment: ApplicationEnvironment) {
    val module = module {
        single { environment }
    }
}