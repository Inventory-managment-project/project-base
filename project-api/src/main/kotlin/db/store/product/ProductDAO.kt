package mx.unam.fciencias.ids.eq1.db.store.product

import mx.unam.fciencias.ids.eq1.model.store.product.Product
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class ProductDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ProductDAO>(ProductTable) {
        fun productDaoToModel(dao: ProductDAO) = Product(
            dao.id.value,
            dao.name,
            dao.description ?: "",
            dao.price,
            dao.barcode ?: "",
            dao.wholesalePrice,
            dao.retailPrice,
            dao.createdAt.epochSecond,
            dao.stock,
            dao.minAllowStock
        )
    }
    var name by ProductTable.name
    var price by ProductTable.price
    var barcode by ProductTable.barcode
    var description by ProductTable.description
    var wholesalePrice by ProductTable.wholesalePrice
    var retailPrice by ProductTable.retailPrice
    var createdAt by ProductTable.createdAt
    var stock by ProductTable.stock
    var minAllowStock by ProductTable.minAllowStock
}