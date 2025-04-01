package mx.unam.fciencias.ids.eq1.db.store.product

import mx.unam.fciencias.ids.eq1.db.store.StoreTable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.timestamp
import org.jetbrains.exposed.sql.javatime.*

object ProductTable  : IntIdTable("products") {
    val storeId = reference("id_store", StoreTable)
    val productId = integer("id_product")
    val name = varchar("name", 255)
    val barcode = varchar("barcode", 255).nullable()
    val description = varchar("description", 255).nullable()
    val stock = decimal("stock", 14, 4)
    val minAllowStock = integer("minAllowStock")
    val createdAt = timestamp("created_at").defaultExpression(CurrentTimestamp)
    val active = bool("active").default(true)

    init {
        uniqueIndex("unique_store_barcode", storeId, barcode)
        uniqueIndex("unique_product_prod", storeId, productId)
        index(isUnique = false, storeId)
    }
}