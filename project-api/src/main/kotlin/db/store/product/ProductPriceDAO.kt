package mx.unam.fciencias.ids.eq1.db.store.product

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class ProductPriceDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ProductPriceDAO>(ProductPriceTable)

    var productId by ProductPriceTable.productId
    var timetamp by ProductPriceTable.timestamp
    var price by ProductPriceTable.price
    var retailPrice by ProductPriceTable.retailPrice
    var wholesalePrice by ProductPriceTable.wholesalePrice
}