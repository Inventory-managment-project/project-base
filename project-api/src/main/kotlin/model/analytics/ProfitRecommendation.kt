package mx.unam.fciencias.ids.eq1.model.analytics

/**
 * Recomendación para mejorar ganancias
 */
data class ProfitRecommendation(
    val type: RecommendationType,
    val productId: Int? = null,
    val productName: String? = null,
    val description: String,
    val potentialImpact: String,
    val expectedROI: String,
    val priority: Int
)