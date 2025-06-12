package mx.unam.fciencias.ids.eq1.model.analytics.repository

import mx.unam.fciencias.ids.eq1.model.analytics.*
import mx.unam.fciencias.ids.eq1.model.analytics.ReportFormat

/**
 * Repositorio para obtener análisis basados en datos de ventas
 */
interface AnalyticsRepository {
    /**
     * Obtiene un análisis completo de ventas para un período específico
     */
    suspend fun getSalesAnalytics(config: Analytics): SalesAnalytics

    /**
     * Obtiene los productos más vendidos en un período
     */
    suspend fun getBestSellingProducts(config: Analytics): List<ProductAnalytics>

    /**
     * Obtiene los productos menos vendidos en un período
     */
    suspend fun getWorstSellingProducts(config: Analytics): List<ProductAnalytics>

    /**
     * Obtiene análisis de rentabilidad (productos con más ganancias)
     */
    suspend fun getProfitAnalysis(config: Analytics): List<ProductAnalytics>

    /**
     * Obtiene análisis de pérdidas (productos que generan pérdidas)
     */
    suspend fun getLossAnalysis(config: Analytics): List<ProductAnalytics>

    /**
     * Obtiene la tendencia de ventas diarias para un período
     */
    suspend fun getSalesTrend(config: Analytics): List<DailySaleData>

    /**
     * Obtiene el rendimiento de ventas por categoría
     */
    suspend fun getCategoryPerformance(config: Analytics): Map<String, Double>

    /**
     * Exporta un reporte en el formato especificado
     */
    suspend fun exportReport(
        config: Analytics,
        format: ReportFormat
    ): ByteArray
}