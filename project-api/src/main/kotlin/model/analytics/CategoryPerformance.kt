package mx.unam.fciencias.ids.eq1.model.analytics

import java.math.BigDecimal

/**
 * Rendimiento por categoría
 */
data class CategoryPerformance(
    val categoryName: String,
    val totalProducts: Int,
    val totalQuantitySold: Int,
    val totalRevenue: BigDecimal,
    val averagePrice: BigDecimal,
    val marketShare: Double, // Porcentaje del total de ventas
    val profitability: BigDecimal
)