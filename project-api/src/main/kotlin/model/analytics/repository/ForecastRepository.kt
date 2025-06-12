package mx.unam.fciencias.ids.eq1.model.analytics.repository

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import mx.unam.fciencias.ids.eq1.db.utils.suspendTransaction
import mx.unam.fciencias.ids.eq1.model.analytics.Analytics
import mx.unam.fciencias.ids.eq1.model.analytics.ProductAnalytics
import mx.unam.fciencias.ids.eq1.model.analytics.RiskLevel
import mx.unam.fciencias.ids.eq1.model.analytics.SalesForecast
import org.jetbrains.exposed.sql.Database
import org.koin.core.annotation.Factory
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import kotlin.math.max

/**
 * Repositorio para generar pronósticos  y evitar pérdidas
 */
@Factory
class ForecastRepository(
    private val database: Database,
    private val analyticsRepository: DBAnalyticsRepository
) {

    suspend fun generateSalesForecast(config: Analytics): List<SalesForecast> = suspendTransaction(database) {
        var products : List<ProductAnalytics> = emptyList()
        runBlocking { launch { products = getBestSellingProducts(config) } }
        val daysInPeriod = java.time.temporal.ChronoUnit.DAYS.between(
            Instant.ofEpochMilli(config.startDate.atStartOfDay().toInstant(ZoneId.systemDefault() as ZoneOffset?).toEpochMilli()).atZone(ZoneId.systemDefault()).toLocalDate(),
            Instant.ofEpochMilli(config.endDate.atStartOfDay().toInstant(ZoneId.systemDefault() as ZoneOffset?).toEpochMilli()).atZone(ZoneId.systemDefault()).toLocalDate()
        )

        products.map { product ->
            val averageDailySales = if (daysInPeriod > 0) product.totalQuantitySold.toDouble() / daysInPeriod else 0.0
            val predictedDemand = averageDailySales * 30 // Pronóstico para 30 días
            val daysUntilStockout = if (averageDailySales > 0) (product.stockLevel / averageDailySales).toInt() else null

            val riskLevel = when {
                daysUntilStockout == null || daysUntilStockout > 30 -> RiskLevel.LOW
                daysUntilStockout > 14 -> RiskLevel.MEDIUM
                daysUntilStockout > 7 -> RiskLevel.HIGH
                else -> RiskLevel.CRITICAL
            }

            val recommendedOrder = max(0, (predictedDemand - product.stockLevel).toInt())

            SalesForecast(
                productId = product.productId,
                productName = product.productName,
                currentStock = product.stockLevel,
                averageDailySales = averageDailySales,
                predictedDemand = predictedDemand,
                daysUntilStockout = daysUntilStockout,
                recommendedOrderQuantity = recommendedOrder,
                riskLevel = riskLevel
            )
        }
    }

    private suspend fun getBestSellingProducts(config: Analytics): List<ProductAnalytics> {
        // Use the existing analyticsRepository to get the best selling products
        // which already returns ProductAnalytics with all required fields
        val salesAnalytics = analyticsRepository.getSalesAnalytics(config)
        return salesAnalytics.bestSellingProducts
    }
}