package mx.unam.fciencias.ids.eq1.service.store

import mx.unam.fciencias.ids.eq1.model.store.CreateStoreRequest
import mx.unam.fciencias.ids.eq1.model.store.Store
import mx.unam.fciencias.ids.eq1.model.store.repository.StoreRepository
import mx.unam.fciencias.ids.eq1.model.user.User
import org.koin.core.annotation.Single

/**
 * Implementation of [StoreService] that interacts with a store repository and database manager.
 *
 * @property storeRepository Handles data persistence for store-related operations.
 */
@Single
class DBStoreService(
    private val storeRepository: StoreRepository,
) : StoreService {
    override suspend fun createStore(storeRequest: CreateStoreRequest, user: User): Boolean {
        try {
             storeRepository.add(storeRequest, user)
            return true
        } catch (e: Exception) { return false }
    }
    override suspend fun getStoresByOwner(ownerId: Int): List<Store> {
        return storeRepository.getByOwnerId(ownerId)
    }

    override suspend fun getStoreById(id: Int): Store? {
        return storeRepository.getById(id)
    }

    override suspend fun deleteStore(id: Int): Boolean {
        return storeRepository.delete(id)
    }
}