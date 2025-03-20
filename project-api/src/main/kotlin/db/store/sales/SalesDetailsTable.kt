package mx.unam.fciencias.ids.eq1.db.store.sales

import mx.unam.fciencias.ids.eq1.db.store.product.ProductTable
import org.jetbrains.exposed.sql.Table

object SalesDetailsTable : Table("SalesDetailsTable"){
    val salesId = reference("salesId", SalesTable)
    val productId = reference("productId", ProductTable)
    val quantity = decimal("quantity", 10, 4)

    override val primaryKey = PrimaryKey(salesId, productId)
}