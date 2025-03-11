package mx.unam.fciencias.ids.eq1.service.store

import mx.unam.fciencias.ids.eq1.model.store.CreateStoreRequest
import mx.unam.fciencias.ids.eq1.model.user.User

interface StoreService {
    suspend fun createStore(storeRequest: CreateStoreRequest, user: User): Boolean
}