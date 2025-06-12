package mx.unam.fciencias.ids.eq1.service.store.sales

import mx.unam.fciencias.ids.eq1.db.store.sales.PAYMENTMETHOD
import mx.unam.fciencias.ids.eq1.model.store.product.coupon.CouponsRepository
import mx.unam.fciencias.ids.eq1.model.store.sales.Sale
import mx.unam.fciencias.ids.eq1.model.store.sales.repository.SalesRepository
import org.koin.core.annotation.Factory
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf
import java.math.BigDecimal

/**
 * Implementation of [SaleService] that interacts with a sales repository.
 *
 * @property storeId The ID of the store for which sales operations are performed.
 */
@Factory
class DBSaleService(storeId: Int) : SaleService, KoinComponent {

    private val salesRepository: SalesRepository by inject {  parametersOf(storeId) }
    private val couponsRepository : CouponsRepository by inject {  parametersOf(storeId) }

    override suspend fun getSaleById(id: Int): Sale? {
        return salesRepository.getById(id)
    }

    override suspend fun getAllSales(): List<Sale> {
        return salesRepository.getAll()
    }

    override suspend fun addSale(sale: Sale): Int {
        return salesRepository.add(sale)
    }

    override suspend fun updateSale(sale: Sale): Boolean {
        return salesRepository.update(sale)
    }

    override suspend fun updateSale(id: Int, sale: Sale): Boolean {
        salesRepository.getById(id) ?: return false

        val updatedSale = sale.copy(id = id)

        return salesRepository.update(updatedSale)
    }

    override suspend fun deleteSale(id: Int): Boolean {
        return salesRepository.delete(id)
    }

    override suspend fun deleteAllSales(): Boolean {
        return salesRepository.deleteAll()
    }

    override suspend fun getSalesByPaymentMethod(paymentMethod: PAYMENTMETHOD): List<Sale> {
        return salesRepository.getByPaymentMethod(paymentMethod)
    }

    override suspend fun getSalesByDateRange(startDate: Long, endDate: Long): List<Sale> {
        return salesRepository.getByDateRange(startDate, endDate)
    }

    override suspend fun calculateTotalRevenue(startDate: Long, endDate: Long): BigDecimal {
        return salesRepository.getTotalRevenue(startDate, endDate)
    }

    override suspend fun getSalesByProductId(productId: Int): List<Sale> {
        return salesRepository.getSalesByProductId(productId)
    }

    override suspend fun generateSalesReports(period: String): Map<String, Any> {
        TODO("Not yet implemented")
    }
}