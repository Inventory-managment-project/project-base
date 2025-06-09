package mx.unam.fciencias.ids.eq1.service.analytics

import GraphicsRepository
import mx.unam.fciencias.ids.eq1.model.analytics.Analytics

/**
 * Servicio de Graphics
 */
class GraphicsService(
    private val graphicsRepository: GraphicsRepository
) {
    suspend fun generateBestSellingProductsChart(config: mx.unam.fciencias.ids.eq1.model.analitycs.Analytics) =
        graphicsRepository.generateBestSellingProductsChart(config)

    suspend fun generateSalesTrendChart(config: mx.unam.fciencias.ids.eq1.model.analitycs.Analytics) =
        graphicsRepository.generateSalesTrendChart(config)

    suspend fun generateCategoryPerformanceChart(config: mx.unam.fciencias.ids.eq1.model.analitycs.Analytics) =
        graphicsRepository.generateCategoryPerformanceChart(config)

    suspend fun generateProfitLossChart(config: mx.unam.fciencias.ids.eq1.model.analitycs.Analytics) =
        graphicsRepository.generateProfitLossChart(config)

    suspend fun generateDashboard(config: mx.unam.fciencias.ids.eq1.model.analitycs.Analytics) =
        graphicsRepository.generateDashboard(config)

    suspend fun generateCustomChart(
        data: Map<String, Any>,
        chartType: mx.unam.fciencias.ids.eq1.model.analytics.repository.ChartType,
        title: String,
        xLabel: String,
        yLabel: String
    ) = graphicsRepository.generateCustomChart(data, chartType, title, xLabel, yLabel)
}