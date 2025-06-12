package mx.unam.fciencias.ids.eq1.model.store.product.coupon

import mx.unam.fciencias.ids.eq1.db.store.product.coupons.CouponDAO
import mx.unam.fciencias.ids.eq1.db.store.product.coupons.CouponDAO.Companion.couponDaoToModel
import mx.unam.fciencias.ids.eq1.db.store.product.coupons.CouponsTable
import mx.unam.fciencias.ids.eq1.db.store.StoreTable
import mx.unam.fciencias.ids.eq1.db.store.product.ProductTable
import mx.unam.fciencias.ids.eq1.db.utils.suspendTransaction
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.annotation.Factory
import java.time.Instant

/**
 * Database-backed implementation of [CouponsRepository] using Exposed.
 * This repository handles all coupon-related database operations.
 */
@Factory
class DBCouponsRepository(
    private val database: Database,
    private val storeId: Int
) : CouponsRepository {

    /**
     * Initializes the database schema for coupons table if it doesn't exist
     */
    init {
        transaction(database) {
            SchemaUtils.create(CouponsTable)
        }
    }

    /**
     * Retrieves a coupon by its code
     */
    override suspend fun getById(couponCode: String): Coupon? = suspendTransaction(database) {
        CouponDAO
            .find { (CouponsTable.storeID eq storeId) and (CouponsTable.couponCode eq couponCode) }
            .singleOrNull()
            ?.let(::couponDaoToModel)
    }

    /**
     * Retrieves all coupons for the store
     */
    override suspend fun getAll(): List<Coupon> = suspendTransaction(database) {
        CouponDAO
            .find { CouponsTable.storeID eq storeId }
            .map(::couponDaoToModel)
    }

    /**
     * Adds a new coupon to the store
     */
    override suspend fun add(coupon: CreateCouponRequest): Boolean = suspendTransaction(database) {
        try {
            // Validate that exactly one of discount or discountAmount is provided
            val hasDiscount = coupon.discount != null
            val hasDiscountAmount = coupon.discountAmount != null

            if (hasDiscount == hasDiscountAmount) {
                return@suspendTransaction false // Both null or both not null
            }

            CouponDAO.new {
                couponCode = coupon.couponCode
                description = coupon.description
                category = coupon.category
                discount = coupon.discount
                discountAmount = coupon.discountAmount
                aplicableProduct = coupon.prodId?.let { EntityID(it, ProductTable) }
                validFrom = coupon.validFrom?.let { Instant.ofEpochSecond(it) } ?: Instant.now()
                validUntil = coupon.validUntil?.let { Instant.ofEpochSecond(it) }
                storeID = EntityID(this@DBCouponsRepository.storeId, StoreTable)
            }
            true
        } catch (_: Exception) {
            false
        }
    }

    /**
     * Updates an existing coupon
     */
    override suspend fun update(couponCode: String, coupon: UpdateCouponRequest): Boolean = suspendTransaction(database) {
        try {
            val couponDao = CouponDAO
                .find { (CouponsTable.storeID eq storeId) and (CouponsTable.couponCode eq couponCode) }
                .singleOrNull()
                ?: return@suspendTransaction false

            // Validate discount constraint if either is being updated
            val newDiscount = coupon.discount ?: couponDao.discount
            val newDiscountAmount = coupon.discountAmount ?: couponDao.discountAmount
            val hasDiscount = newDiscount != null
            val hasDiscountAmount = newDiscountAmount != null

            if (hasDiscount == hasDiscountAmount) {
                return@suspendTransaction false // Both null or both not null
            }

            couponDao.apply {
                if (coupon.description != null) description = coupon.description
                if (coupon.category != null) category = coupon.category
                if (coupon.discount != null) {
                    discount = coupon.discount
                    discountAmount = null
                }
                if (coupon.discountAmount != null) {
                    discountAmount = coupon.discountAmount
                    discount = null
                }
                if (coupon.prodId != null) aplicableProduct = EntityID(coupon.prodId, ProductTable)
                if (coupon.validFrom != null) validFrom = Instant.ofEpochSecond(coupon.validFrom)
                if (coupon.validUntil != null) validUntil = Instant.ofEpochSecond(coupon.validUntil)
            }
            true
        } catch (_: Exception) {
            false
        }
    }

    /**
     * Deletes a coupon by code
     */
    override suspend fun delete(couponCode: String): Boolean = suspendTransaction(database) {
        CouponDAO
            .find { (CouponsTable.storeID eq storeId) and (CouponsTable.couponCode eq couponCode) }
            .singleOrNull()
            ?.let {
                it.delete()
                true
            } ?: false
    }

    /**
     * Retrieves coupons by category
     */
    override suspend fun getByCategory(category: String): List<Coupon> = suspendTransaction(database) {
        CouponDAO
            .find { (CouponsTable.storeID eq storeId) and (CouponsTable.category eq category) }
            .map(::couponDaoToModel)
    }

    /**
     * Retrieves currently valid coupons
     */
    override suspend fun getValidCoupons(): List<Coupon> = suspendTransaction(database) {
        val now = Instant.now()
        CouponDAO
            .find {
                (CouponsTable.storeID eq storeId) and
                        (CouponsTable.validFrom lessEq now) and
                        ((CouponsTable.validUntil.isNull()) or (CouponsTable.validUntil greaterEq now))
            }
            .map(::couponDaoToModel)
    }

    /**
     * Retrieves coupons applicable to a specific product
     */
    override suspend fun getCouponsForProduct(productId: Int): List<Coupon> = suspendTransaction(database) {
        val now = Instant.now()
        CouponDAO
            .find {
                (CouponsTable.storeID eq storeId) and
                        ((CouponsTable.aplicableProduct.isNull()) or (CouponsTable.aplicableProduct eq productId)) and
                        (CouponsTable.validFrom lessEq now) and
                        ((CouponsTable.validUntil.isNull()) or (CouponsTable.validUntil greaterEq now))
            }
            .map(::couponDaoToModel)
    }

    /**
     * Checks if a coupon exists and is valid
     */
    override suspend fun isValidCoupon(couponCode: String): Boolean = suspendTransaction(database) {
        val now = Instant.now()
        !CouponDAO
            .find {
                (CouponsTable.storeID eq storeId) and
                        (CouponsTable.couponCode eq couponCode) and
                        (CouponsTable.validFrom lessEq now) and
                        ((CouponsTable.validUntil.isNull()) or (CouponsTable.validUntil greaterEq now))
            }
            .empty()
    }

    /**
     * Checks if a coupon exists by code
     */
    override suspend fun existsById(couponCode: String): Boolean = suspendTransaction(database) {
        !CouponDAO
            .find { (CouponsTable.storeID eq storeId) and (CouponsTable.couponCode eq couponCode) }
            .empty()
    }

    /**
     * Retrieves coupons that were valid at a specific timestamp
     */
    override suspend fun getCouponsValidAtTime(timestamp: Long): List<Coupon> = suspendTransaction(database) {
        val instant = Instant.ofEpochSecond(timestamp)
        CouponDAO
            .find {
                (CouponsTable.storeID eq storeId) and
                        (CouponsTable.validFrom lessEq instant) and
                        ((CouponsTable.validUntil.isNull()) or (CouponsTable.validUntil greaterEq instant))
            }
            .mapNotNull { CouponDAO.couponDaoToModelAtTime(it, timestamp) }
    }

    /**
     * Retrieves percentage-based discount coupons
     */
    override suspend fun getPercentageCoupons(): List<Coupon> = suspendTransaction(database) {
        CouponDAO
            .find { (CouponsTable.storeID eq storeId) and (CouponsTable.discount.isNotNull()) }
            .map(::couponDaoToModel)
    }

    /**
     * Retrieves fixed amount discount coupons
     */
    override suspend fun getAmountCoupons(): List<Coupon> = suspendTransaction(database) {
        CouponDAO
            .find { (CouponsTable.storeID eq storeId) and (CouponsTable.discountAmount.isNotNull()) }
            .map(::couponDaoToModel)
    }
}
