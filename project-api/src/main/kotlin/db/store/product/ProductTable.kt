package mx.unam.fciencias.ids.eq1.db.store.product

import mx.unam.fciencias.ids.eq1.db.store.StoreTable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.timestamp
import org.jetbrains.exposed.sql.javatime.*

object ProductTable  : IntIdTable("products") {
    val storeId = reference("id_store", StoreTable)
    val productId = integer("id_product")
    val name = varchar("name", 255)
    val price = decimal("price", 10, 2)
    val barcode = varchar("barcode", 255).nullable()
    val description = varchar("description", 255).nullable()
    val wholesalePrice = decimal("wholesalePrice", 10, 2)
    val retailPrice = decimal("retailPrice", 10, 2)
    val stock = integer("stock")
    val minAllowStock = integer("minAllowStock")
    val createdAt = timestamp("created_at").defaultExpression(CurrentTimestamp)

    init {
        uniqueIndex("unique_store_barcode", storeId, barcode)
        uniqueIndex("unique_product_prod", storeId, productId)
        index(isUnique = false, storeId)
    }
}