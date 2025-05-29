package mx.unam.fciencias.ids.eq1.service.store.reports

import mx.unam.fciencias.ids.eq1.model.store.reports.DateRangeSalesReport
import mx.unam.fciencias.ids.eq1.model.store.reports.ProductSalesReport

interface ReportService {
    suspend fun getProductSalesReport(storeId: Int, startDate: Long, endDate: Long): List<ProductSalesReport>
    suspend fun getDateRangeSalesReport(storeId: Int, startDate: Long, endDate: Long): DateRangeSalesReport
}