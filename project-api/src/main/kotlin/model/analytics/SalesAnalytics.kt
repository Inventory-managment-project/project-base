package mx.unam.fciencias.ids.eq1.model.analytics

import kotlinx.serialization.Serializable
import mx.unam.fciencias.ids.eq1.model.analitycs.TimePeriod

/**
 * Análisis de ventas por período
 */
@Serializable
data class SalesAnalytics(
    val period: TimePeriod,
    val startDate: Long,
    val endDate: Long,
    val totalSales: Double,
    val totalProfit: Double,
    val totalLoss: Double,
    val totalItems: Int,
    val averageTicketValue: Double,
    val bestSellingProducts: List<ProductAnalytics>,
    val worstSellingProducts: List<ProductAnalytics>,
    val salesByCategory: Map<String, Double>,
    val profitByCategory: Map<String, Double>,
    val salesTrend: List<DailySaleData>
)
