package mx.unam.fciencias.ids.eq1.model.analytics.repository

import mx.unam.fciencias.ids.eq1.db.store.StoreTable
import mx.unam.fciencias.ids.eq1.db.store.product.ProductDAO
import mx.unam.fciencias.ids.eq1.db.store.product.ProductTable
import mx.unam.fciencias.ids.eq1.db.store.sales.SalesDAO
import mx.unam.fciencias.ids.eq1.db.store.sales.SalesDetailsTable
import mx.unam.fciencias.ids.eq1.db.store.sales.SalesTable
import mx.unam.fciencias.ids.eq1.db.utils.suspendTransaction
import mx.unam.fciencias.ids.eq1.model.analytics.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.koin.core.annotation.Factory
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import kotlin.math.max
/**
 * Repositorio para generar pronósticos y evitar pérdidas
 */
@Factory
class ForecastRepository(
    private val database: Database,
    private val analyticsRepository: DBAnalyticsRepository
) {

    suspend fun generateSalesForecast(config: Analytics): List<SalesForecast> = suspendTransaction(database) {
        val products = getBestSellingProducts(config)
        val daysInPeriod = java.time.temporal.ChronoUnit.DAYS.between(
            Instant.ofEpochMilli(config.startDate).atZone(ZoneId.systemDefault()).toLocalDate(),
            Instant.ofEpochMilli(config.endDate).atZone(ZoneId.systemDefault()).toLocalDate()
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

    private fun Transaction.getBestSellingProducts(config: Analytics) {}
}