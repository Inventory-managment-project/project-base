package mx.unam.fciencias.ids.eq1.model.analytics

import kotlinx.serialization.Serializable

/**
 * Datos de venta por día para gráficos de tendencia
 */
@Serializable
data class DailySaleData(
    val date: Long,
    val sales: Double,
    val profit: Double,
    val transactions: Int
)