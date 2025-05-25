package model

import kotlinx.serialization.Serializable
import mx.unam.fciencias.ids.eq1.db.store.sales.PAYMENTMETHOD
import mx.unam.fciencias.ids.eq1.utils.BigDecimalSerializer
import java.math.BigDecimal
import java.time.LocalDateTime

// ============= ANALYTICS PARA PUNTO DE VENTA =============

@Serializable
data class SalesAnalytics(
    val totalSales: Long,
    val totalRevenue: @Serializable(BigDecimalSerializer::class) BigDecimal,
    val averageTicket: @Serializable(BigDecimalSerializer::class) BigDecimal,
    val salesByPaymentMethod: Map<String, Long>,
    val revenueByPaymentMethod: Map<String, @Serializable(BigDecimalSerializer::class) BigDecimal>,
    val topSellingProducts: List<ProductSalesMetric>,
    val salesByHour: List<HourlySalesMetric>,
    val salesByDay: List<DailySalesMetric>,
    val lowStockProducts: List<LowStockProduct>,
    val salesGrowth: List<SalesGrowthMetric>
)

@Serializable
data class ProductSalesMetric(
    val productId: Int,
    val productName: String,
    val quantitySold: @Serializable(BigDecimalSerializer::class) BigDecimal,
    val revenue: @Serializable(BigDecimalSerializer::class) BigDecimal,
    val salesCount: Long,
    val averagePrice: @Serializable(BigDecimalSerializer::class) BigDecimal
)

@Serializable
data class HourlySalesMetric(
    val hour: Int,
    val salesCount: Long,
    val revenue: @Serializable(BigDecimalSerializer::class) BigDecimal
)

@Serializable
data class DailySalesMetric(
    val date: String,
    val salesCount: Long,
    val revenue: @Serializable(BigDecimalSerializer::class) BigDecimal,
    val averageTicket: @Serializable(BigDecimalSerializer::class) BigDecimal,
    val transactionsByPaymentMethod: Map<String, Long>
)

@Serializable
data class LowStockProduct(
    val productId: Int,
    val productName: String,
    val currentStock: @Serializable(BigDecimalSerializer::class) BigDecimal,
    val minAllowStock: Int,
    val stockLevel: StockLevel,
    val lastSaleDate: String?
)

@Serializable
enum class StockLevel {
    CRITICAL,    // Stock <= 0
    LOW,         // Stock <= minAllowStock
    WARNING,     // Stock <= minAllowStock * 1.5
    NORMAL       // Stock > minAllowStock * 1.5
}

@Serializable
data class SalesGrowthMetric(
    val date: String,
    val salesCount: Long,
    val revenue: @Serializable(BigDecimalSerializer::class) BigDecimal,
    val growthPercentage: Double,
    val revenueGrowthPercentage: Double
)

@Serializable
data class InventoryAnalytics(
    val totalProducts: Long,
    val totalStockValue: @Serializable(BigDecimalSerializer::class) BigDecimal,
    val lowStockCount: Long,
    val outOfStockCount: Long,
    val averageStockLevel: Double,
    val topValueProducts: List<ProductValueMetric>,
    val stockMovement: List<StockMovementMetric>
)

@Serializable
data class ProductValueMetric(
    val productId: Int,
    val productName: String,
    val stock: @Serializable(BigDecimalSerializer::class) BigDecimal,
    val retailPrice: @Serializable(BigDecimalSerializer::class) BigDecimal,
    val totalValue: @Serializable(BigDecimalSerializer::class) BigDecimal
)

@Serializable
data class StockMovementMetric(
    val productId: Int,
    val productName: String,
    val initialStock: @Serializable(BigDecimalSerializer::class) BigDecimal,
    val currentStock: @Serializable(BigDecimalSerializer::class) BigDecimal,
    val totalSold: @Serializable(BigDecimalSerializer::class) BigDecimal,
    val turnoverRate: Double
)

@Serializable
data class RealtimeMetrics(
    val todaySales: Long,
    val todayRevenue: @Serializable(BigDecimalSerializer::class) BigDecimal,
    val currentHourSales: Long,
    val averageTicketToday: @Serializable(BigDecimalSerializer::class) BigDecimal,
    val topSellingProductToday: ProductSalesMetric?,
    val recentSales: List<RecentSaleMetric>
)

@Serializable
data class RecentSaleMetric(
    val saleId: Int,
    val total: @Serializable(BigDecimalSerializer::class) BigDecimal,
    val paymentMethod: PAYMENTMETHOD,
    val timestamp: String,
    val productCount: Int
)

@Serializable
data class AnalyticsFilter(
    val startDate: String? = null,
    val endDate: String? = null,
    val storeId: Int? = null,
    val paymentMethod: PAYMENTMETHOD? = null,
    val productId: Int? = null
) 