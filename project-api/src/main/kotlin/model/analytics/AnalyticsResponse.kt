package mx.unam.fciencias.ids.eq1.model.analytics

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import mx.unam.fciencias.ids.eq1.model.analitycs.AnalyticsType
import mx.unam.fciencias.ids.eq1.model.analitycs.TimePeriod
import java.time.Instant

@Serializable
data class AnalyticsResponse(
    val type: AnalyticsType,
    val period: TimePeriod,
    val startDate: Long,
    val endDate: Long,
    val data: JsonElement, // Datos específicos del análisis solicitado
    val generatedAt: Long = Instant.now().toEpochMilli()
)