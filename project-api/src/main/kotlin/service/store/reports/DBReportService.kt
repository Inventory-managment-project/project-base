package mx.unam.fciencias.ids.eq1.service.store.reports

import mx.unam.fciencias.ids.eq1.model.store.reports.DateRangeSalesReport
import mx.unam.fciencias.ids.eq1.model.store.reports.ProductSalesReport
import mx.unam.fciencias.ids.eq1.service.store.sales.SalesService
import mx.unam.fciencias.ids.eq1.service.store.product.ProductService
import org.koin.core.annotation.Single
import java.math.BigDecimal
import java.math.RoundingMode

@Single
class DBReportService(
    private val salesService: SalesService,
    private val productService: ProductService
) : ReportService {

    override suspend fun getProductSalesReport(
        storeId: Int,
        startDate: Long,
        endDate: Long
    ): List<ProductSalesReport> {
        val sales = salesService.getSalesByDateRange(storeId, startDate, endDate)
        val productSales = mutableMapOf<Int, MutableList<Pair<Int, BigDecimal>>>()

        // Usamos bucles 'for' para garantizar que seguimos en el cuerpo de la coroutine
        for (sale in sales) {
            // Aquí sale.products debe ser algo como Map<Int, Int> o List<Pair<Int, Int>>
            for ((productId, unitPrice) in sale.products) {
              // cada aparición es una unidad vendida
              productSales
                .getOrPut(productId) { mutableListOf() }
                .add(1 to unitPrice)
            }
        }

        // Construimos la lista final
        return productSales.map { (productId, salesList) ->
            val product = productService.getProductById(productId)!!
            val totalQuantity = salesList.sumOf { it.first }
            val totalRevenue = salesList.sumOf { (qty, price) -> price * BigDecimal(qty) }
            val averagePrice = if (totalQuantity > 0)
                totalRevenue.divide(BigDecimal(totalQuantity), 2, RoundingMode.HALF_UP)
            else BigDecimal.ZERO

            ProductSalesReport(
                productId       = productId,
                productName     = product.name,
                totalQuantitySold = totalQuantity,
                totalRevenue    = totalRevenue,
                averagePrice    = averagePrice
            )
        }
    }

    override suspend fun getDateRangeSalesReport(
        storeId: Int,
        startDate: Long,
        endDate: Long
    ): DateRangeSalesReport {
        val productReports = getProductSalesReport(storeId, startDate, endDate)
        return DateRangeSalesReport(
            startDate    = startDate,
            endDate      = endDate,
            totalSales   = productReports.sumOf { it.totalQuantitySold },
            totalRevenue = productReports.fold(BigDecimal.ZERO) { acc, rpt -> acc + rpt.totalRevenue },
            products     = productReports
        )
    }
}
