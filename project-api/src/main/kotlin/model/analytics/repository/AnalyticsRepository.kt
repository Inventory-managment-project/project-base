package mx.unam.fciencias.ids.eq1.model.analytics.repository

import mx.unam.fciencias.ids.eq1.model.analytics.*
import mx.unam.fciencias.ids.eq1.model.analytics.ReportFormat

/**
 * Repositorio para obtener el análisis basados en datos de  ventas
 */
interface AnalyticsRepository {
    /**
     * Obtiene un análisis completo de ventas para un período específico
     */
    suspend fun getSalesAnalytics(config: AnalyticsRepository): SalesAnalytics

    /**
     * Obtiene los productos más vendidos en un período
     */
    suspend fun getBestSellingProducts(config: AnalyticsRepository): List<ProductAnalytics>

    /**
     * Obtiene los productos menos vendidos en un período
     */
    suspend fun getWorstSellingProducts(config: Analytics): List<ProductAnalytics>

    /**
     * Obtiene análisis de rentabilidad (productos con más ganancias)
     */
    suspend fun getProfitAnalysis(config: AnalyticsRepository): List<ProductAnalytics>

    /**
     * Obtiene análisis de pérdidas (productos que generan pérdidas)
     */
    suspend fun getLossAnalysis(config: AnalyticsRepository): List<ProductAnalytics>

    /**
     * Obtiene la tendencia de ventas diarias para un período
     */
    suspend fun getSalesTrend(config: AnalyticsRepository): List<DailySaleData>

    /**
     * Obtiene el rendimiento de ventas por categoría
     */
    suspend fun getCategoryPerformance(config: AnalyticsRepository): Map<String, Double>

    /**
     * Exporta un reporte en el formato especificado
     */
    suspend fun exportReport(
        config: Analytics,
        format: ReportFormat
    ): ByteArray
}
