package mx.unam.fciencias.ids.eq1.model.analytics

import java.time.LocalDate

/**
 * Configuración para análisis de datos
 */
data class Analytics(
    val storeId: Int,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val limit: Int? = null,
    val includeCategories: List<String>? = null,
    val excludeCategories: List<String>? = null,
    val minQuantity: Int? = null,
    val maxResults: Int? = null
)