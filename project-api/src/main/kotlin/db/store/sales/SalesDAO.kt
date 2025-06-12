package mx.unam.fciencias.ids.eq1.db.store.sales

import mx.unam.fciencias.ids.eq1.db.store.product.ProductDAO
import mx.unam.fciencias.ids.eq1.model.store.sales.Sales
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class SalesDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<SalesDAO>(SalesTable) {
        fun salesDaoToModel(dao: SalesDAO): Sales {
            val productPairs = transaction {
                SalesDetailsTable
                    .selectAll()
                    .where { SalesDetailsTable.salesId eq dao.id }
                    .map { row ->
                        val productId = row[SalesDetailsTable.productId]
                        val product = ProductDAO[productId].productId
                        val quantity = row[SalesDetailsTable.quantity]
                        Pair(product, quantity)
                    }
            }

            return Sales(
                id = dao.salesId,
                total = dao.total,
                paymentmethod = dao.paymentMethod.name,
                created = dao.createdAt.epochSecond,
                products = productPairs,
                subtotal = dao.subtotal
            )
        }
    }


    var salesId by SalesTable.salesId
    var storeId by SalesTable.storeId
    var total by SalesTable.total
    var paymentMethod by SalesTable.paymentMethod
    var createdAt by SalesTable.created
    var subtotal by SalesTable.total
}