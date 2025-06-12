package mx.unam.fciencias.ids.eq1.model.analytics

import java.math.BigDecimal

/**
 * Análisis de producto individual
 */
data class ProductAnalytics(
    val productId: Int,
    val productName: String,
    val category: String?,
    val totalQuantitySold: Int,
    val totalRevenue: BigDecimal,
    val averagePrice: BigDecimal,
    val profitMargin: BigDecimal,
    val stockLevel: Int,
    val salesFrequency: Double, // Ventas por día
    val rankByRevenue: Int,
    val rankByQuantity: Int
)