package mx.unam.fciencias.ids.eq1.model.analitycs

import kotlinx.serialization.Serializable

/**
 * Configuración para personalizar el análisis
 */
@Serializable
data class Analytics(
    val type: AnalyticsType,
    val period: TimePeriod,
    val startDate: Long, // Epoch timestamp
    val endDate: Long,   // Epoch timestamp
    val limit: Int = 10, // Número de resultados (para listados)
    val includeCategories: List<Int>? = null,
    val excludeCategories: List<Int>? = null,
    val storeId: Int
)
