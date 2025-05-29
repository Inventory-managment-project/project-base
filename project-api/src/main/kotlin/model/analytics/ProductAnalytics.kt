package mx.unam.fciencias.ids.eq1.model.analytics

import kotlinx.serialization.Serializable

/**
 * Representa un producto en análisis de ventas
 */
@Serializable
data class ProductAnalytics(
    val productId: Int,
    val name: String,
    val quantitySold: Int,
    val revenue: Double,
    val profit: Double,
    val costValue: Double,
    val salesPercentage: Double // Porcentaje sobre el total de ventas
)
