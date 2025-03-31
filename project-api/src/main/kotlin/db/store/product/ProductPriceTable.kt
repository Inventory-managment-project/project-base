package mx.unam.fciencias.ids.eq1.db.store.product

import org.jetbrains.exposed.dao.id.CompositeIdTable
import org.jetbrains.exposed.sql.javatime.CurrentTimestamp
import org.jetbrains.exposed.sql.javatime.timestamp

object ProductPriceTable : CompositeIdTable("product_price") {
    val productId = reference("id_product", ProductTable)
    val price = decimal("price", 10, 2)
    val wholesalePrice = decimal("wholesalePrice", 10, 2)
    val retailPrice = decimal("retailPrice", 10, 2)
    val timestamp = timestamp("timestamp").defaultExpression(CurrentTimestamp).entityId()

    override val primaryKey = PrimaryKey(productId, timestamp)
}