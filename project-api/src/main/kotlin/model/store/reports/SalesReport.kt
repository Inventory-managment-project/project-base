package mx.unam.fciencias.ids.eq1.model.store.reports

import kotlinx.serialization.Serializable
import java.math.BigDecimal

@Serializable
data class ProductSalesReport(
    val productId: Int,
    val productName: String,
    val totalQuantitySold: Int,
    val totalRevenue: BigDecimal,
    val averagePrice: BigDecimal
)

@Serializable
data class DateRangeSalesReport(
    val startDate: Long,
    val endDate: Long,
    val totalSales: Int,
    val totalRevenue: BigDecimal,
    val products: List<ProductSalesReport>
)