package mx.unam.fciencias.ids.eq1.model.store.product.coupon

import mx.unam.fciencias.ids.eq1.model.store.product.coupon.Coupon

/**
 * Repository interface defining operations for coupon data management.
 */
interface CouponsRepository {
    /**
     * Retrieves a coupon by its code.
     */
    suspend fun getById(couponCode: String): Coupon?

    /**
     * Retrieves all coupons for the store.
     */
    suspend fun getAll(): List<Coupon>

    /**
     * Adds a new coupon.
     */
    suspend fun add(coupon: CreateCouponRequest): Boolean

    /**
     * Updates an existing coupon.
     */
    suspend fun update(couponCode: String, coupon: UpdateCouponRequest): Boolean

    /**
     * Deletes a coupon by code.
     */
    suspend fun delete(couponCode: String): Boolean

    /**
     * Retrieves coupons by category.
     */
    suspend fun getByCategory(category: String): List<Coupon>

    /**
     * Retrieves currently valid coupons.
     */
    suspend fun getValidCoupons(): List<Coupon>

    /**
     * Retrieves coupons applicable to a specific product.
     */
    suspend fun getCouponsForProduct(productId: Int): List<Coupon>

    /**
     * Checks if a coupon exists and is valid.
     */
    suspend fun isValidCoupon(couponCode: String): Boolean

    /**
     * Checks if a coupon exists by code.
     */
    suspend fun existsById(couponCode: String): Boolean

    /**
     * Retrieves coupons that were valid at a specific timestamp.
     */
    suspend fun getCouponsValidAtTime(timestamp: Long): List<Coupon>

    /**
     * Retrieves percentage-based discount coupons.
     */
    suspend fun getPercentageCoupons(): List<Coupon>

    /**
     * Retrieves fixed amount discount coupons.
     */
    suspend fun getAmountCoupons(): List<Coupon>
}