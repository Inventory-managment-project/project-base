package mx.unam.fciencias.ids.eq1.db

import org.jetbrains.exposed.sql.Database

interface StoreInventoryDatabaseManager {
    fun getStoreDatabase(storeId: Int): Database
    suspend fun createStoreDatabase(storeId : Int): Database
}