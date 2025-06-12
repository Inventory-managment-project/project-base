package mx.unam.fciencias.ids.eq1.model.analytics

import java.time.LocalDate

/**
 * Período de análisis
 */
data class wAnalyticsPeriod(
    val startDate: LocalDate,
    val endDate: LocalDate,
    val durationInDays: Long
)