package mx.unam.fciencias.ids.eq1.db.store.sales

import mx.unam.fciencias.ids.eq1.db.store.product.ProductDAO
import mx.unam.fciencias.ids.eq1.model.store.sales.Sales
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant

class SalesDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<SalesDAO>(SalesTable) {
        fun salesDaoToModel(dao: SalesDAO): Sales {
            val productPairs = transaction {
                SalesDetailsTable
                    .select( SalesDetailsTable.salesId eq dao.id )
                    .map { row ->
                        val productId = row[SalesDetailsTable.productId]
                        val product = ProductDAO[productId].let { ProductDAO.productDaoToModel(it) }
                        val quantity = row[SalesDetailsTable.quantity]
                        Pair(product, quantity)
                    }
            }

            return Sales(
                id = dao.id.value,
                total = dao.total,
                paymentmethod = dao.paymentMethod,
                created = dao.createdAt.epochSecond,
                productId = productPairs,
                subtotal = dao.subtotal
            )
        }
    }

    var total by SalesTable.total
    var paymentMethod by SalesTable.paymentMethod
    var createdAt by SalesTable.created
    var subtotal by SalesTable.total
}