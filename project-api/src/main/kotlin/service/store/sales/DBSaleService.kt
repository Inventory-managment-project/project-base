package mx.unam.fciencias.ids.eq1.service.store.sales

import mx.unam.fciencias.ids.eq1.db.store.sales.PAYMENTMETHOD
import mx.unam.fciencias.ids.eq1.model.store.product.coupon.Coupon
import mx.unam.fciencias.ids.eq1.model.store.product.coupon.CouponsRepository
import mx.unam.fciencias.ids.eq1.model.store.product.repository.ProductRepository
import mx.unam.fciencias.ids.eq1.model.store.sales.Sale
import mx.unam.fciencias.ids.eq1.model.store.sales.repository.SalesRepository
import org.koin.core.annotation.Factory
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf
import org.postgresql.jdbc.PgResultSet.toBigDecimal
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Implementation of [SaleService] that interacts with a sales repository.
 * Now includes coupon management functionality for applying discounts to sales.
 *
 * @property storeId The ID of the store for which sales operations are performed.
 */
@Factory
class DBSaleService(storeId: Int) : SaleService, KoinComponent {

    private val salesRepository: SalesRepository by inject { parametersOf(storeId) }
    private val couponsRepository: CouponsRepository by inject { parametersOf(storeId) }

    private val productRepository : ProductRepository by inject { parametersOf(storeId) }

    override suspend fun getSaleById(id: Int): Sale? {
        return salesRepository.getById(id)
    }

    override suspend fun getAllSales(): List<Sale> {
        return salesRepository.getAll()
    }

    override suspend fun addSale(sale: Sale): Int {
        return salesRepository.add(sale)
    }

    /**
     * Adds a sale with coupon discount applied
     * @param sale The sale to add
     * @param couponCode Optional coupon code to apply discount
     * @return The ID of the created sale, or -1 if failed
     */
    override suspend fun addSaleWithCoupon(sale: Sale, couponCode: String?): Int {
        val discountedSale = if (couponCode != null) {
            applyCouponToSale(sale, couponCode)
        } else {
            sale
        }
        return salesRepository.add(discountedSale ?: sale)
    }

    /**
     * Applies a coupon discount to a sale
     * @param sale The original sale
     * @param couponCode The coupon code to apply
     * @return The sale with discount applied, or null if coupon is invalid
     */
    override suspend fun applyCouponToSale(sale: Sale, couponCode: String): Sale? {
        val coupon = couponsRepository.getById(couponCode) ?: return null

        if (!couponsRepository.isValidCoupon(couponCode)) {
            return null
        }

        val discount = calculateCouponDiscount(sale, coupon)

        return sale.copy(
            total = (sale.total - discount).max(BigDecimal.ZERO)
        )
    }

    /**
     * Calculates the discount amount for a given sale and coupon
     * @param sale The sale to calculate discount for
     * @param coupon The coupon to apply
     * @return The discount amount
     */
    private suspend fun calculateCouponDiscount(sale: Sale, coupon: Coupon): BigDecimal {
        val applicableTotal = if (coupon.prodId != null) {
            calculateProductSpecificTotal(sale, coupon.prodId)
        } else {
            sale.total
        }

        return when {
            coupon.discount != null -> {
                applicableTotal.multiply(coupon.discount)
                    .divide(BigDecimal(100), 2, RoundingMode.HALF_UP)
            }
            coupon.discountAmount != null -> {
                coupon.discountAmount.min(applicableTotal)
            }
            else -> BigDecimal.ZERO
        }
    }

    /**
     * Calculates the total for a specific product in the sale
     * @param sale The sale containing products
     * @param productId The product ID to calculate total for
     * @return The total amount for the specified product
     */
    private suspend fun calculateProductSpecificTotal(sale: Sale, productId: Int): BigDecimal {
        // This would need to be implemented based on how products are stored in Sale
        // For now, returning the full total as placeholder
        // In a real implementation, you'd filter sale.products by productId and calculate

        return productRepository.getById(productId)?.retailPrice
            ?.times(sale.products.first { it.first == productId }.second)
            ?: 0.toBigDecimal()
    }

    /**
     * Validates if a coupon can be applied to a sale
     * @param couponCode The coupon code to validate
     * @param sale The sale to apply coupon to
     * @return True if coupon can be applied, false otherwise
     */
    override suspend fun validateCouponForSale(couponCode: String, sale: Sale): Boolean {
        return couponsRepository.isValidCoupon(couponCode)
    }

    /**
     * Gets all valid coupons that can be applied to a sale
     * @param sale The sale to get applicable coupons for
     * @return List of applicable coupons
     */
    suspend fun getApplicableCoupons(sale: Sale): List<Coupon> {
        val validCoupons = couponsRepository.getValidCoupons()

        return validCoupons.filter { coupon ->
            if (coupon.prodId != null) {
                sale.products.any { it.first == coupon.prodId }
            } else {
                true
            }
        }
    }

    /**
     * Gets coupons applicable to specific products in the sale
     * @param productIds List of product IDs in the sale
     * @return List of applicable coupons
     */
    suspend fun getCouponsForProducts(productIds: List<Int>): List<Coupon> {
        val allApplicableCoupons = mutableListOf<Coupon>()

        for (productId in productIds) {
            val productCoupons = couponsRepository.getCouponsForProduct(productId)
            allApplicableCoupons.addAll(productCoupons)
        }

        val generalCoupons = couponsRepository.getValidCoupons().filter { it.prodId == null }
        allApplicableCoupons.addAll(generalCoupons)

        return allApplicableCoupons.distinctBy { it.couponCode }
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