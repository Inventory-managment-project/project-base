package mx.unam.fciencias.ids.eq1.service.store.product

import mx.unam.fciencias.ids.eq1.model.store.product.Product

interface ProductService {
    suspend fun getAllProducts(): List<Product>

    suspend fun getProductById(id: Int): Product?

    suspend fun addProduct(product: Product): Int

    suspend fun updateProduct(product: Product): Boolean

    suspend fun deleteProduct(id: Int): Boolean
}