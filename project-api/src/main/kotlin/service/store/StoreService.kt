package mx.unam.fciencias.ids.eq1.service.store

import mx.unam.fciencias.ids.eq1.model.store.CreateStoreRequest
import mx.unam.fciencias.ids.eq1.model.store.Store
import mx.unam.fciencias.ids.eq1.model.user.User
import org.jetbrains.exposed.sql.Database

/**
 * Service interface for handling store-related operations.
 */
interface StoreService {

    /**
     * Creates a new store based on the provided request data and associates it with the given user.
     *
     * @param storeRequest The request data containing store details.
     * @param user The user who owns or manages the store.
     * @return `true` if the store was successfully created, otherwise `false`.
     */
    suspend fun createStore(storeRequest: CreateStoreRequest, user: User): Boolean

    suspend fun getStoresByOwner(ownerId: Int): List<Store>

    suspend fun getStoreById(id: Int): Store?

    suspend fun deleteStore(id: Int): Boolean

    suspend fun getStoreDatabase(id: Int): Database
}