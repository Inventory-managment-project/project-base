package mx.unam.fciencias.ids.eq1.model.store.repository

import mx.unam.fciencias.ids.eq1.db.store.StoreTable
import mx.unam.fciencias.ids.eq1.db.utils.suspendTransaction
import mx.unam.fciencias.ids.eq1.model.store.Store
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.annotation.Single
import mx.unam.fciencias.ids.eq1.db.store.StoreDAO
import mx.unam.fciencias.ids.eq1.db.store.StoreDAO.Companion.storeDaoToModel
import mx.unam.fciencias.ids.eq1.db.user.UserDAO
import mx.unam.fciencias.ids.eq1.db.user.UserTable
import mx.unam.fciencias.ids.eq1.model.store.CreateStoreRequest
import mx.unam.fciencias.ids.eq1.model.store.UpdateStoreRequest
import mx.unam.fciencias.ids.eq1.model.user.User
import org.jetbrains.exposed.dao.id.EntityID

@Single
class DBStoreRepository(private val database: Database) : StoreRepository {

    init {
        transaction(database) {
            SchemaUtils.create(StoreTable)
        }
    }

    override suspend fun getById(id: Int): Store? = suspendTransaction(database) {
        StoreDAO
            .find { StoreTable.id eq id }
            .limit(1)
            .map(::storeDaoToModel)
            .firstOrNull()
    }

    override suspend fun getByOwnerId(ownerId: Int): List<Store> = suspendTransaction(database) {
        StoreDAO
            .find { StoreTable.owner eq ownerId }
            .map(::storeDaoToModel)
    }

    override suspend fun getAll(): List<Store> = suspendTransaction(database) {
        StoreDAO
            .all()
            .map(::storeDaoToModel)
    }

    override suspend fun add(store: CreateStoreRequest, ownerUser: User): Int = suspendTransaction(database) {
        if (UserDAO.findById(ownerUser.id) == null) {
            return@suspendTransaction -1
        }
        try {
            val storeDao = StoreDAO.new {
                name = store.name
                address = store.address
                owner = EntityID(ownerUser.id, UserTable)
            }
            storeDao.id.value
        } catch ( e : Exception) { return@suspendTransaction -1 }

    }

    override suspend fun delete(id: Int): Boolean = suspendTransaction(database) {
        StoreDAO
            .find { StoreTable.id eq id }
            .singleOrNull()
            .apply {
                if(this == null) return@suspendTransaction false
                this.delete()
            }
        return@suspendTransaction true
    }

    override suspend fun update(id: Int, store: UpdateStoreRequest): Boolean = suspendTransaction(database) {
        StoreDAO
            .find { StoreTable.id eq id }
            .singleOrNull()
            .apply {
                if (this == null) { return@suspendTransaction false }
                if(store.newName != null) { this.name = store.newName }
                if(store.newAddress != null) { this.address = store.newAddress }
            }
        return@suspendTransaction true

    }
}