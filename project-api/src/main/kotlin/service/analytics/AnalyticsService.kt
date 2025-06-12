package mx.unam.fciencias.ids.eq1.service.analytics


import mx.unam.fciencias.ids.eq1.model.analytics.*
import mx.unam.fciencias.ids.eq1.model.analytics.repository.DBAnalyticsRepository
import mx.unam.fciencias.ids.eq1.model.analytics.repository.ForecastRepository
import mx.unam.fciencias.ids.eq1.model.analytics.repository.LetsPlotGraphicsRepository
import org.koin.core.annotation.Service
import java.time.LocalDate
import java.time.ZoneOffset

/**
 * Servicio principal para análisis y generación de reportes
 */
@Service
class AnalyticsService(
    private val analyticsRepository: DBAnalyticsRepository,
    private val forecastRepository: ForecastRepository,
    private val graphicsRepository: LetsPlotGraphicsRepository
) {

    /**
     * Genera un análisis completo de ventas para el período especificado
     */
    suspend fun generateSalesReport(
        storeId: Int,
        startDate: LocalDate,
        endDate: LocalDate,
        includeCategories: List<String>? = null,
        maxResults: Int = 20
    ): SalesAnalytics {
        val config = Analytics(
            storeId = storeId,
            startDate = startDate.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli(),
            endDate = endDate.atTime(23, 59, 59).toInstant(ZoneOffset.UTC).toEpochMilli(),
            includeCategories = includeCategories,
            maxResults = maxResults
        )
        return analyticsRepository.getSalesAnalytics(config)
    }

    /**
     * Genera pronósticos para evitar pérdidas por falta de stock
     */
    suspend fun generateStockForecast(
        storeId: Int,
        daysBack: Long = 30,
        forecastDays: Int = 30
    ): List<SalesForecast> {
        val endDate = LocalDate.now()
        val startDate = endDate.minusDays(daysBack)

        val config = Analytics(
            storeId = storeId,
            startDate = startDate.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli(),
            endDate = endDate.atTime(23, 59, 59).toInstant(ZoneOffset.UTC).toEpochMilli()
        )

        return forecastRepository.generateSalesForecast(config)
    }

    /**
     * Identifica productos con riesgo de pérdidas
     */
    suspend fun identifyRiskProducts(
        storeId: Int,
        daysBack: Long = 30
    ): List<ProductRiskAnalysis> {
        val forecast = generateStockForecast(storeId, daysBack)

        return forecast.map { product ->
            val riskFactors = mutableListOf<String>()

            when (product.riskLevel) {
                RiskLevel.CRITICAL -> riskFactors.add("Stock crítico - se agotará en ${product.daysUntilStockout} días")
                RiskLevel.HIGH -> riskFactors.add("Stock bajo - se agotará en ${product.daysUntilStockout} días")
                RiskLevel.MEDIUM -> riskFactors.add("Monitorear stock - se agotará en ${product.daysUntilStockout} días")
                RiskLevel.LOW -> riskFactors.add("Stock estable")
            }

            if (product.averageDailySales < 0.1) {
                riskFactors.add("Producto de muy baja rotación")
            }

            if (product.currentStock > product.averageDailySales * 60) {
                riskFactors.add("Posible sobrestock - más de 60 días de inventario")
            }

            ProductRiskAnalysis(
                productId = product.productId,
                productName = product.productName,
                riskLevel = product.riskLevel,
                riskFactors = riskFactors,
                recommendedAction = getRecommendedAction(product),
                priority = getPriority(product.riskLevel)
            )
        }.sortedBy { it.priority }
    }

    /**
     * Genera recomendaciones para mejorar ganancias
     */
    suspend fun generateProfitRecommendations(
        storeId: Int,
        daysBack: Long = 30
    ): List<ProfitRecommendation> {
        val endDate = LocalDate.now()
        val startDate = endDate.minusDays(daysBack)

        val config = Analytics(
            storeId = storeId,
            startDate = startDate.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli(),
            endDate = endDate.atTime(23, 59, 59).toInstant(ZoneOffset.UTC).toEpochMilli(),
            maxResults = 50
        )

        val bestSelling = DBAnalyticsRepository.getBestSellingProducts(config)
        val worstSelling = analyticsRepository.getWorstSellingProducts(config)
        val categoryPerformance = analyticsRepository.getCategoryPerformance(config)

        val recommendations = mutableListOf<ProfitRecommendation>()

        // Recomendaciones basadas en productos más vendidos
        bestSelling.take(10).forEach { product ->
            if (product.stockLevel < product.totalQuantitySold * 0.5) {
                recommendations.add(
                    ProfitRecommendation(
                        type = RecommendationType.INCREASE_STOCK,
                        productId = product.productId,
                        productName = product.productName,
                        description = "Aumentar stock del producto más vendido",
                        potentialImpact = "Alto",
                        expectedROI = calculateExpectedROI(product),
                        priority = 1
                    )
                )
            }
        }

        // Recomendaciones para productos de baja rotación
        worstSelling.filter { it.stockLevel > it.totalQuantitySold * 3 }.forEach { product ->
            recommendations.add(
                ProfitRecommendation(
                    type = RecommendationType.REDUCE_PRICE,
                    productId = product.productId,
                    productName = product.productName,
                    description = "Considerar descuento para producto de baja rotación",
                    potentialImpact = "Medio",
                    expectedROI = "Reducir pérdidas por inventario obsoleto",
                    priority = 3
                )
            )
        }

        // Recomendaciones por categoría
        categoryPerformance.entries.sortedByDescending { it.value }.take(3).forEach { (category, revenue) ->
            recommendations.add(
                ProfitRecommendation(
                    type = RecommendationType.FOCUS_CATEGORY,
                    description = "Enfocarse en categoría '$category' con ingresos de $${String.format("%.2f", revenue)}",
                    potentialImpact = "Alto",
                    expectedROI = "Incremento del 15-25% en ventas de la categoría",
                    priority = 2
                )
            )
        }

        return recommendations.sortedBy { it.priority }
    }

    /**
     * Genera gráficos de análisis
     */
    suspend fun generateAnalyticsChart(
        storeId: Int,
        chartType: AnalyticsChartType,
        daysBack: Long = 30
    ): ByteArray {
        val endDate = LocalDate.now()
        val startDate = endDate.minusDays(daysBack)

        val config = Analytics(
            storeId = storeId,
            startDate = startDate.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli(),
            endDate = endDate.atTime(23, 59, 59).toInstant(ZoneOffset.UTC).toEpochMilli(),
            maxResults = 15
        )

        return when (chartType) {
            AnalyticsChartType.BEST_SELLING -> graphicsRepository.generateBestSellingProductsChart(config)
            AnalyticsChartType.SALES_TREND -> graphicsRepository.generateSalesTrendChart(config)
            AnalyticsChartType.CATEGORY_PERFORMANCE -> graphicsRepository.generateCategoryPerformanceChart(config)
            AnalyticsChartType.PROFIT_LOSS -> graphicsRepository.generateProfitLossChart(config)
            AnalyticsChartType.DASHBOARD -> graphicsRepository.generateDashboard(config)
        }
    }

    /**
     * Exporta reporte completo
     */
    suspend fun exportAnalyticsReport(
        storeId: Int,
        format: ReportFormat,
        daysBack: Long = 30
    ): ByteArray {
        val endDate = LocalDate.now()
        val startDate = endDate.minusDays(daysBack)

        val config = Analytics(
            storeId = storeId,
            startDate = startDate.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli(),
            endDate = endDate.atTime(23, 59, 59).toInstant(ZoneOffset.UTC).toEpochMilli()
        )

        return analyticsRepository.exportReport(config, format)
    }

    private fun getRecommendedAction(forecast: SalesForecast): String {
        return when (forecast.riskLevel) {
            RiskLevel.CRITICAL -> "ACCIÓN INMEDIATA: Reabastecer ${forecast.recommendedOrderQuantity} unidades"
            RiskLevel.HIGH -> "Planificar pedido de ${forecast.recommendedOrderQuantity} unidades esta semana"
            RiskLevel.MEDIUM -> "Considerar pedido de ${forecast.recommendedOrderQuantity} unidades"
            RiskLevel.LOW -> "Monitorear stock regularmente"
        }
    }

    private fun getPriority(riskLevel: RiskLevel): Int {
        return when (riskLevel) {
            RiskLevel.CRITICAL -> 1
            RiskLevel.HIGH -> 2
            RiskLevel.MEDIUM -> 3
            RiskLevel.LOW -> 4
        }
    }

    private fun calculateExpectedROI(product: ProductAnalytics): String {
        val potentialSales = product.totalQuantitySold * 1.2 // 20% más ventas con mejor stock
        val additionalRevenue = (potentialSales - product.totalQuantitySold) * product.averagePrice.toDouble()
        return "Incremento potencial de $${String.format("%.2f", additionalRevenue)}"
    }
}