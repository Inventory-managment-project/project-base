package mx.unam.fciencias.ids.eq1.service.store

import mx.unam.fciencias.ids.eq1.db.SQLStoreInventoryDatabaseManager
import mx.unam.fciencias.ids.eq1.model.store.CreateStoreRequest
import mx.unam.fciencias.ids.eq1.model.store.repository.StoreRepository
import mx.unam.fciencias.ids.eq1.model.user.User
import org.koin.core.annotation.Single

@Single
class DBStoreService(
    private val storeRepository: StoreRepository,
    private val storeDatabaseManagerImpl: SQLStoreInventoryDatabaseManager
) : StoreService {
    override suspend fun createStore(storeRequest: CreateStoreRequest, user: User): Boolean {
        try {
            storeDatabaseManagerImpl.createStoreDatabase(storeRequest ,user)
            return true
        } catch (e: Exception) { return false }
    }

}