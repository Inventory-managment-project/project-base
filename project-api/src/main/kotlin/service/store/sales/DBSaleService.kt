package mx.unam.fciencias.ids.eq1.service.store.sales

import mx.unam.fciencias.ids.eq1.db.store.sales.PAYMENTMETHOD
import mx.unam.fciencias.ids.eq1.model.store.sales.Sales
import mx.unam.fciencias.ids.eq1.model.store.sales.repository.SalesRepository
import org.koin.core.annotation.Factory
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit

/**
 * Implementation of [SaleService] that interacts with a sales repository.
 *
 * @property storeId The ID of the store for which sales operations are performed.
 */
@Factory
class DBSaleService(private val storeId: Int) : SaleService, KoinComponent {

    private val salesRepository: SalesRepository by inject { return@inject parametersOf(storeId) }

    override suspend fun getSaleById(id: Int): Sales? {
        return salesRepository.getById(id)
    }

    override suspend fun getAllSales(): List<Sales> {
        return salesRepository.getAll()
    }

    override suspend fun createSale(sale: Sales): Int {
        return salesRepository.add(sale)
    }

    override suspend fun updateSale(sale: Sales): Boolean {
        return salesRepository.update(sale)
    }

    override suspend fun updateSale(id: Int, sale: Sales): Boolean {
        // First check if the sale exists
        val existingSale = salesRepository.getById(id) ?: return false

        // Create a copy of the sale with the correct ID
        val updatedSale = sale.copy(id = id)

        return salesRepository.update(updatedSale)
    }

    override suspend fun deleteSale(id: Int): Boolean {
        return salesRepository.delete(id)
    }

    override suspend fun deleteAllSales(): Boolean {
        return salesRepository.deleteAll()
    }

    override suspend fun getSalesByPaymentMethod(paymentMethod: PAYMENTMETHOD): List<Sales> {
        return salesRepository.getByPaymentMethod(paymentMethod)
    }

    override suspend fun getSalesByDateRange(startDate: Long, endDate: Long): List<Sales> {
        return salesRepository.getByDateRange(startDate, endDate)
    }

    override suspend fun calculateTotalRevenue(startDate: Long, endDate: Long): BigDecimal {
        return salesRepository.getTotalRevenue(startDate, endDate)
    }

    override suspend fun getSalesByProductId(productId: Int): List<Sales> {
        return salesRepository.getSalesByProductId(productId)
    }

    override suspend fun generateSalesReports(period: String): Map<String, Any> {
        TODO("Not yet implemented")
    }
}