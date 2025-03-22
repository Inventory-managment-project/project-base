package mx.unam.fciencias.ids.eq1.service.store.product

import mx.unam.fciencias.ids.eq1.model.store.product.Product

interface ProductService {
    suspend fun getAllProducts(): List<Product>

    suspend fun getProductById(id: Int): Product?

    suspend fun addProduct(product: Product): Int

    suspend fun updateProduct(product: Product): Boolean

    suspend fun deleteProductById(id: Int): Boolean

    suspend fun deleteProductByBarcode(barcode: String): Boolean

    suspend fun getProductByBarcode(string: String): Product?
}