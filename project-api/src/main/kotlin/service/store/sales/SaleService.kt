package mx.unam.fciencias.ids.eq1.service.store.sales

import mx.unam.fciencias.ids.eq1.db.store.sales.PAYMENTMETHOD
import mx.unam.fciencias.ids.eq1.model.store.sales.Sales
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
    suspend fun getSaleById(id: Int): Sales?

    /**
     * Retrieves all sales.
     *
     * @return A list of all sales.
     */
    suspend fun getAllSales(): List<Sales>

    /**
     * Creates a new sale.
     *
     * @param sale The sale to be created.
     * @return The ID of the newly created sale.
     */
    suspend fun createSale(sale: Sales): Int

    /**
     * Updates an existing sale.
     *
     * @param sale The sale with updated information.
     * @return True if the update was successful, false otherwise.
     */
    suspend fun updateSale(sale: Sales): Boolean

    /**
     * Updates an existing sale by ID.
     *
     * @param id The ID of the sale to update.
     * @param sale The updated sale data.
     * @return True if the update was successful, false otherwise.
     */
    suspend fun updateSale(id: Int, sale: Sales): Boolean

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
    suspend fun getSalesByPaymentMethod(paymentMethod: PAYMENTMETHOD): List<Sales>

    /**
     * Retrieves sales within a date range.
     *
     * @param startDate The start date of the range (as Unix timestamp).
     * @param endDate The end date of the range (as Unix timestamp).
     * @return A list of sales within the specified date range.
     */
    suspend fun getSalesByDateRange(startDate: Long, endDate: Long): List<Sales>

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
    suspend fun getSalesByProductId(productId: Int): List<Sales>

    /**
     * Generates sales reports based on the specified period.
     *
     * @param period The period for which to generate reports (daily, weekly, monthly, all).
     * @return A map containing the report data.
     */
    suspend fun generateSalesReports(period: String): Map<String, Any>
}