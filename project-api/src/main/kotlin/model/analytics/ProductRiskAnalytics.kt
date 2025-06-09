package mx.unam.fciencias.ids.eq1.model.analytics

/**
 * Análisis de riesgo de productos
 */
data class ProductRiskAnalysis(
    val productId: Int,
    val productName: String,
    val riskLevel: RiskLevel,
    val riskFactors: List<String>,
    val recommendedAction: String,
    val priority: Int
)