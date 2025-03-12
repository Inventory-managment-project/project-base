package mx.unam.fciencias.ids.eq1.service.store

import mx.unam.fciencias.ids.eq1.db.SQLStoreInventoryDatabaseManager
import mx.unam.fciencias.ids.eq1.model.store.CreateStoreRequest
import mx.unam.fciencias.ids.eq1.model.store.repository.StoreRepository
import mx.unam.fciencias.ids.eq1.model.user.User
import org.koin.core.annotation.Single

/**
 * Implementation of [StoreService] that interacts with a store repository and database manager.
 *
 * @property storeRepository Handles data persistence for store-related operations.
 * @property storeDatabaseManager Manages the creation of store-specific databases.
 */
@Single
class DBStoreService(
    private val storeRepository: StoreRepository,
    private val storeDatabaseManager: SQLStoreInventoryDatabaseManager
) : StoreService {
    override suspend fun createStore(storeRequest: CreateStoreRequest, user: User): Boolean {
        try {
            val newStoreId = storeRepository.add(storeRequest, user)
            storeDatabaseManager.createStoreDatabase(newStoreId)
            return true
        } catch (e: Exception) { return false }
    }
}