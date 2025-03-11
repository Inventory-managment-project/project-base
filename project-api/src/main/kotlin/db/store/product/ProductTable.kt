package mx.unam.fciencias.ids.eq1.db.store.product

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.timestamp
import org.jetbrains.exposed.sql.javatime.*

object ProductTable  : IntIdTable("products") {
    val name = varchar("name", 255)
    val price = decimal("price", 10, 2)
    val barcode = varchar("barcode", 255).nullable().uniqueIndex()
    val description = varchar("description", 255).nullable()
    val wholesalePrice = decimal("wholesalePrice", 10, 2)
    val retailPrice = decimal("retailPrice", 10, 2)
    val stock = integer("stock")
    val minAllowStock = integer("minAllowStock")
    val createdAt = timestamp("created_at").defaultExpression(CurrentTimestamp)
}