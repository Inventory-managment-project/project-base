package mx.unam.fciencias.ids.eq1.db

import mx.unam.fciencias.ids.eq1.model.store.CreateStoreRequest
import mx.unam.fciencias.ids.eq1.model.user.User
import org.jetbrains.exposed.sql.Database

interface StoreInventoryDatabaseManager {
    fun getStoreDatabase(storeId: String): Database
    suspend fun createStoreDatabase(createStoreRequest: CreateStoreRequest, user: User): Database
}