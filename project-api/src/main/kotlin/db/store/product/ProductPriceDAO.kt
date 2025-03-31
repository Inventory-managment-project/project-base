package mx.unam.fciencias.ids.eq1.db.store.product

import org.jetbrains.exposed.dao.CompositeEntity
import org.jetbrains.exposed.dao.CompositeEntityClass
import org.jetbrains.exposed.dao.id.CompositeID
import org.jetbrains.exposed.dao.id.EntityID

class ProductPriceDAO(id: EntityID<CompositeID>) : CompositeEntity(id) {
    companion object : CompositeEntityClass<ProductPriceDAO>(ProductPriceTable)

    var productId by ProductPriceTable.productId
    var timestamp by ProductPriceTable.timestamp
    var price by ProductPriceTable.price
    var retailPrice by ProductPriceTable.retailPrice
    var wholesalePrice by ProductPriceTable.wholesalePrice
}