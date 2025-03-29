package mx.unam.fciencias.ids.eq1.model.store.sales.repository

import mx.unam.fciencias.ids.eq1.db.store.sales.PAYMENTMETHOD
import mx.unam.fciencias.ids.eq1.model.store.sales.Sale
import java.math.BigDecimal

/**
 * Repository interface defining operations for sales data management.
 */
interface SalesRepository {

    /**
     * Retrieves a sale by its ID.
     */
    suspend fun getById(id: Int): Sale?

    /**
     * Retrieves all sales.
     */
    suspend fun getAll(): List<Sale>

    /**
     * Adds a new sale.
     */
    suspend fun add(sale: Sale): Int

    /**
     * Updates an existing sale.
     */
    suspend fun update(sale: Sale): Boolean

    /**
     * Deletes a sale by ID.
     */
    suspend fun delete(id: Int): Boolean

    /**
     * Deletes all sales.
     */
    suspend fun deleteAll(): Boolean

    /**
     * Retrieves sales by payment method.
     */
    suspend fun getByPaymentMethod(paymentMethod: PAYMENTMETHOD): List<Sale>

    /**
     * Retrieves sales within a date range.
     */
    suspend fun getByDateRange(startDate: Long, endDate: Long): List<Sale>

    /**
     * Calculates total revenue for a specific period.
     */
    suspend fun getTotalRevenue(startDate: Long, endDate: Long): BigDecimal

    /**
     * Retrieves sales that contain a specific product.
     */
    suspend fun getSalesByProductId(productId: Int): List<Sale>
}