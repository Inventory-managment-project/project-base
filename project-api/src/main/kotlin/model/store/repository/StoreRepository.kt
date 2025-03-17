package mx.unam.fciencias.ids.eq1.model.store.repository

import mx.unam.fciencias.ids.eq1.model.store.CreateStoreRequest
import mx.unam.fciencias.ids.eq1.model.store.Store
import mx.unam.fciencias.ids.eq1.model.store.UpdateStoreRequest
import mx.unam.fciencias.ids.eq1.model.user.User

interface StoreRepository {
    suspend fun getById(id: Int): Store?
    suspend fun getByOwnerId(ownerId: Int): List<Store>
    suspend fun getAll(): List<Store>
    suspend fun add(store: CreateStoreRequest, ownerUser: User): Int
    suspend fun delete(id: Int): Boolean
    suspend fun update(id: Int, store: UpdateStoreRequest): Boolean
}