import mx.unam.fciencias.ids.eq1.model.analytics.repository.AnalyticsRepository
import mx.unam.fciencias.ids.eq1.model.analytics.repository.DBAnalyticsRepository
import mx.unam.fciencias.ids.eq1.model.analytics.repository.GraphicsRepository
import mx.unam.fciencias.ids.eq1.model.analytics.repository.DBGraphicsRepository
import mx.unam.fciencias.ids.eq1.service.AnalyticsService
import mx.unam.fciencias.ids.eq1.service.GraphicsService
import mx.unam.fciencias.ids.eq1.service.analytics.AnalyticsService
import mx.unam.fciencias.ids.eq1.service.analytics.GraphicsService
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.dsl.module

/**
 * Módulo de Koin para Analytics
 */
@Module
@ComponentScan("mx.unam.fciencias.ids.eq1.model.analytics.repository")
class AnalyticsModule

/**
 * Configuración manual de dependencias (alternativa)
 */
val analyticsModule = module {
    // Repositories
    single<AnalyticsRepository> { DBAnalyticsRepository() }
    single<GraphicsRepository> { DBGraphicsRepository(get()) }

    // Services
    single { AnalyticsService(get()) }
    single { GraphicsService(get()) }
}