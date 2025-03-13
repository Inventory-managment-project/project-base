package mx.unam.fciencias.ids.eq1.model.store.product.repository

import mx.unam.fciencias.ids.eq1.model.store.product.Product

interface ProductRepository {
    suspend fun getById(id: Int): Product?
    suspend fun getAll(): List<Product>
    suspend fun add(product: Product): Int
    suspend fun update(product: Product): Boolean
    suspend fun delete(id: Int): Boolean
    suspend fun deleteAll(): Boolean
}