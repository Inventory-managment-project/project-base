package mx.unam.fciencias.ids.eq1.model.store.reports

import kotlinx.serialization.Serializable
import mx.unam.fciencias.ids.eq1.utils.BigDecimalSerializer
import java.math.BigDecimal

@Serializable
data class ProductSalesReport(
    val productId: Int,
    val productName: String,
    val totalQuantitySold: Int,
    @Serializable(BigDecimalSerializer::class) val totalRevenue: BigDecimal,
    @Serializable(BigDecimalSerializer::class) val averagePrice: BigDecimal
)

@Serializable
data class DateRangeSalesReport(
    val startDate: Long,
    val endDate: Long,
    val totalSales: Int,
    @Serializable(BigDecimalSerializer::class) val totalRevenue: BigDecimal,
    val products: List<ProductSalesReport>
)