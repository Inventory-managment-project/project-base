package mx.unam.fciencias.ids.eq1.db.store.sales

import mx.unam.fciencias.ids.eq1.db.store.StoreTable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.CurrentTimestamp
import org.jetbrains.exposed.sql.javatime.timestamp

enum class PAYMENTMETHOD {CARD, CASH}

object SalesTable : IntIdTable("sales"){
    val salesId = integer("salesId")
    val total = decimal("total", 12, 2)
    val paymentMethod = enumeration("paymentMethod", PAYMENTMETHOD::class)
    val storeId = reference("id_store", StoreTable)
    val created = timestamp("created").defaultExpression(CurrentTimestamp)

    init {
        uniqueIndex("unique_sales_id", salesId, storeId)
        index(isUnique = false, storeId)
    }
}
