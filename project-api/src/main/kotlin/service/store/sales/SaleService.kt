package mx.unam.fciencias.ids.eq1.service.store.sales

import mx.unam.fciencias.ids.eq1.db.store.sales.PAYMENTMETHOD
import mx.unam.fciencias.ids.eq1.model.store.sales.Sale
import java.math.BigDecimal

/**
 * Service interface defining operations for sales management.
 */
interface SaleService {
    /**
     * Retrieves a sale by its ID.
     *
     * @param id The unique identifier of the sale.
     * @return The sale if found, null otherwise.
     */
    suspend fun getSaleById(id: Int): Sale?

    /**
     * Retrieves all sales.
     *
     * @return A list of all sales.
     */
    suspend fun getAllSales(): List<Sale>

    /**
     * Adds a new sale.
     *
     * @param sale The sale to be added.
     * @return The ID of the newly added sale.
     */
    suspend fun addSale(sale: Sale): Int

    /**
     * Updates an existing sale.
     *
     * @param sale The sale with updated information.
     * @return True if the update was successful, false otherwise.
     */
    suspend fun updateSale(sale: Sale): Boolean

    /**
     * Deletes a sale by ID.
     *
     * @param id The unique identifier of the sale to delete.
     * @return True if the deletion was successful, false otherwise.
     */
    suspend fun deleteSale(id: Int): Boolean

    /**
     * Deletes all sales.
     *
     * @return True if the deletion was successful, false otherwise.
     */
    suspend fun deleteAllSales(): Boolean

    /**
     * Retrieves sales by payment method.
     *
     * @param paymentMethod The payment method to filter by.
     * @return A list of sales with the specified payment method.
     */
    suspend fun getSalesByPaymentMethod(paymentMethod: PAYMENTMETHOD): List<Sale>

    /**
     * Retrieves sales within a date range.
     *
     * @param startDate The start date of the range (as Unix timestamp).
     * @param endDate The end date of the range (as Unix timestamp).
     * @return A list of sales within the specified date range.
     */
    suspend fun getSalesByDateRange(startDate: Long, endDate: Long): List<Sale>

    /**
     * Calculates total revenue for a specific period.
     *
     * @param startDate The start date of the period (as Unix timestamp).
     * @param endDate The end date of the period (as Unix timestamp).
     * @return The total revenue as a BigDecimal.
     */
    suspend fun calculateTotalRevenue(startDate: Long, endDate: Long): BigDecimal

    /**
     * Retrieves sales that contain a specific product.
     *
     * @param productId The ID of the product to filter by.
     * @return A list of sales containing the specified product.
     */
    suspend fun getSalesByProductId(productId: Int): List<Sale>

    suspend fun updateSale(id: Int, sale: Sale): Boolean

    suspend fun generateSalesReports(period: String): Map<String, Any>
}