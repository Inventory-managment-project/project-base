package mx.unam.fciencias.ids.eq1.model.analytics

/**
 * Datos para forecasting
 */
data class SalesForecast(
    val productId: Int,
    val productName: String,
    val currentStock: Int,
    val averageDailySales: Double,
    val predictedDemand: Double,
    val daysUntilStockout: Int?,
    val recommendedOrderQuantity: Int,
    val riskLevel: RiskLevel,
    val seasonalFactor: Double = 1.0
)