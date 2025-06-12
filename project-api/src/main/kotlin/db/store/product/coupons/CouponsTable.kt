package mx.unam.fciencias.ids.eq1.db.store.product.coupons

import mx.unam.fciencias.ids.eq1.db.store.StoreTable
import mx.unam.fciencias.ids.eq1.db.store.product.ProductTable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.javatime.CurrentTimestamp
import org.jetbrains.exposed.sql.javatime.timestamp
import org.jetbrains.exposed.sql.or

object CouponsTable : IntIdTable("coupons") {
    val couponCode = varchar("id_coupon", 100)
    val description = varchar("description", 255).nullable()
    val category = varchar("category", 255).nullable()
    val createdAt = timestamp("createdAt").defaultExpression(CurrentTimestamp)
    val discount = decimal("", 4, 2).nullable()
    val discountAmount = decimal("stock", 4, 2).nullable()
    val aplicableProduct = reference("id_product", ProductTable).nullable()
    val validFrom = timestamp("validFrom").defaultExpression(CurrentTimestamp)
    val validUntil = timestamp("validUntil").defaultExpression(CurrentTimestamp).nullable()
    val storeID = reference("storeID", StoreTable)

    init {
        uniqueIndex(storeID, couponCode)
        check("discount_xor_amount") {
            (discount.isNotNull() and discountAmount.isNull()) or
            (discount.isNull() and discountAmount.isNotNull())
        }
    }

}