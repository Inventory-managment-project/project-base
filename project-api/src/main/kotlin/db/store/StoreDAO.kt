package mx.unam.fciencias.ids.eq1.db.store

import mx.unam.fciencias.ids.eq1.model.store.Store
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class StoreDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<StoreDAO>(StoreTable) {
        fun storeDaoToModel(dao: StoreDAO) = Store(
            id = dao.id.value,
            name = dao.name,
            address = dao.address,
            createdAt = dao.createdAt.epochSecond,
            owner = dao.owner.value
        )
    }
    var name by StoreTable.name
    var address by StoreTable.address
    var createdAt by StoreTable.created
    var owner by StoreTable.owner
}