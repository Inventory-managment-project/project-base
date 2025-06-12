package mx.unam.fciencias.ids.eq1.model.analytics

import java.math.BigDecimal
import java.time.LocalDate

/**
 * Datos de ventas diarias
 */
data class DailySaleData(
    val date: LocalDate,
    val totalSales: Int,
    val totalRevenue: BigDecimal,
    val averageSaleValue: BigDecimal,
    val uniqueProducts: Int
)