package mx.unam.fciencias.ids.eq1.service.store

import mx.unam.fciencias.ids.eq1.model.store.CreateStoreRequest
import mx.unam.fciencias.ids.eq1.model.user.User

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
}