package mx.unam.fciencias.ids.eq1.di

import org.jetbrains.exposed.sql.Database
import org.koin.dsl.module

class DatabaseModule {
    val module = module {
        single {
            Database.connect(
                url = System.getenv("DATABASE_URL") ?: "jdbc:postgresql://localhost:5432/postgres",
                driver = "org.postgresql.Driver",
                user = System.getenv("DATABASE_USER") ?: "postgres",
                password = System.getenv("DATABASE_PASSWORD") ?: "postgres"
            )
        }
    }
}