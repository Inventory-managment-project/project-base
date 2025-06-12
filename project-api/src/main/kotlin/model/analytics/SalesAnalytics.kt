package mx.unam.fciencias.ids.eq1.model.analytics

import mx.unam.fciencias.ids.eq1.db.store.sales.PAYMENTMETHOD
import java.math.BigDecimal
import java.time.LocalDate
/**
 * Análisis completo de ventas
 */
data class SalesAnalytics(
    val totalSales: Int,
    val totalRevenue: BigDecimal,
    val averageSaleValue: BigDecimal,
    val bestSellingProducts: List<ProductAnalytics>,
    val worstSellingProducts: List<ProductAnalytics>,
    val salesByPaymentMethod: Map<PAYMENTMETHOD, Int>,
    val revenueByPaymentMethod: Map<PAYMENTMETHOD, BigDecimal>,
    val dailySales: List<DailySaleData>,
    val categoryPerformance: Map<String, CategoryPerformance>,
    val period: AnalyticsPeriod
)