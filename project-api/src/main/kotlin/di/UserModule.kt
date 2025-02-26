package mx.unam.fciencias.ids.eq1.di

import mx.unam.fciencias.ids.eq1.model.user.UserRepository
import mx.unam.fciencias.ids.eq1.model.user.PostgresqlUserRepository
import mx.unam.fciencias.ids.eq1.service.UserService
import org.koin.dsl.module

val userModule = module {
    single<UserRepository> { PostgresqlUserRepository() }
    single { UserService(get()) }
}
