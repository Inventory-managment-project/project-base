package mx.unam.fciencias.ids.eq1.db.store.product

import mx.unam.fciencias.ids.eq1.model.store.product.Product
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SortOrder

class ProductDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ProductDAO>(ProductTable) {
        fun productDaoToModel(dao: ProductDAO) : Product {
            return Product(
                dao.productId,
                dao.name,
                dao.description ?: "",
                dao.prices.first().price,
                dao.barcode ?: "",
                dao.prices.first().wholesalePrice,
                dao.prices.first().retailPrice,
                dao.createdAt.epochSecond,
                dao.stock,
                dao.minAllowStock,
                dao.storeId.value
            )
        }
        fun productDaoToModelWithTime(dao: ProductDAO, timestamp : Long) : Product? {
            try {
                val priceAtTime = dao.prices.first { it.timestamp.value.epochSecond <= timestamp }
                return Product(
                    dao.productId,
                    dao.name,
                    dao.description ?: "",
                    priceAtTime.price,
                    dao.barcode ?: "",
                    priceAtTime.wholesalePrice,
                    priceAtTime.retailPrice,
                    dao.createdAt.epochSecond,
                    dao.stock,
                    dao.minAllowStock,
                    dao.storeId.value
                )
            } catch (e: NoSuchElementException) {
                return null
            }
        }
    }

    var productId by ProductTable.productId
    var name by ProductTable.name
    var barcode by ProductTable.barcode
    var description by ProductTable.description
    var createdAt by ProductTable.createdAt
    var stock by ProductTable.stock
    var minAllowStock by ProductTable.minAllowStock
    var storeId by ProductTable.storeId
    var active by ProductTable.active

    val prices by ProductPriceDAO referrersOn ProductPriceTable.productId orderBy listOf(ProductPriceTable.timestamp to SortOrder.DESC)
}