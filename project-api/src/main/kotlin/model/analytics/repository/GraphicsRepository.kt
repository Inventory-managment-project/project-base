
import mx.unam.fciencias.ids.eq1.model.analitycs.Analytics
import mx.unam.fciencias.ids.eq1.model.analytics.*
import mx.unam.fciencias.ids.eq1.model.analytics.repository.ChartType


/**
 * Repositorio para generar gráficos usando lets-plot
 */
interface GraphicsRepository {
    /**
     * Genera un gráfico de barras de los productos más vendidos
     */
    suspend fun generateBestSellingProductsChart(config: Analytics): ByteArray

    /**
     * Genera un gráfico de líneas de tendencia de ventas
     */
    suspend fun generateSalesTrendChart(config: Analytics): ByteArray

    /**
     * Genera un gráfico de pie/dona para el rendimiento por categoría
     */
    suspend fun generateCategoryPerformanceChart(config: Analytics): ByteArray

    /**
     * Genera un gráfico de barras comparativo de ganancias vs pérdidas
     */
    suspend fun generateProfitLossChart(config: Analytics): ByteArray

    /**
     * Genera un dashboard completo con múltiples gráficos
     */
    suspend fun generateDashboard(config: Analytics): ByteArray

    /**
     * Genera un gráfico personalizado basado en parámetros específicos
     */
    suspend fun generateCustomChart(
        data: Map<String, Any>,
        chartType: ChartType,
        title: String,
        xLabel: String,
        yLabel: String
    ): ByteArray
}