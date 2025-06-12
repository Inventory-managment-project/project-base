package mx.unam.fciencias.ids.eq1.db.store.product.coupons

import mx.unam.fciencias.ids.eq1.model.store.product.coupon.Coupon
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.math.BigDecimal

class CouponDAO(id : EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<CouponDAO>(CouponsTable) {
        fun couponDaoToModel(dao: CouponDAO): Coupon {
            return Coupon(
                couponCode = dao.couponCode,
                description = dao.description,
                category = dao.category,
                createdAt = dao.createdAt.epochSecond,
                discount = dao.discount,
                discountAmount = dao.discountAmount,
                prodId = dao.aplicableProduct?.value,
                valid = dao.isValid()
            )
        }

        fun couponDaoToModelAtTime(dao: CouponDAO, timestamp: Long): Coupon? {
            val timestampInstant = java.time.Instant.ofEpochSecond(timestamp)
            val validFromInstant = dao.validFrom
            val validUntilInstant = dao.validUntil

            val wasValid = timestampInstant >= validFromInstant &&
                    (validUntilInstant == null || timestampInstant <= validUntilInstant)

            return if (wasValid) {
                Coupon(
                    couponCode = dao.couponCode,
                    description = dao.description,
                    category = dao.category,
                    createdAt = dao.createdAt.epochSecond,
                    discount = dao.discount,
                    discountAmount = dao.discountAmount,
                    prodId = dao.aplicableProduct?.value,
                    valid = true
                )
            } else {
                null
            }
        }
    }

    var couponCode by CouponsTable.couponCode
    var description by CouponsTable.description
    var category by CouponsTable.category
    var createdAt by CouponsTable.createdAt
    var discount by CouponsTable.discount
    var discountAmount by CouponsTable.discountAmount
    var aplicableProduct by CouponsTable.aplicableProduct
    var validFrom by CouponsTable.validFrom
    var validUntil by CouponsTable.validUntil

    var storeID by CouponsTable.storeID

    fun isValid(): Boolean {
        val now = java.time.Instant.now()
        return now >= validFrom && (validUntil == null || now <= validUntil!!)
    }

    fun isPercentageDiscount(): Boolean = discount != null

    fun isAmountDiscount(): Boolean = discountAmount != null

    fun getDiscountValue(): BigDecimal? = discount ?: discountAmount
}