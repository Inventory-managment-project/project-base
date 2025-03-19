package mx.unam.fciencias.ids.eq1.model.store.product.repository

import mx.unam.fciencias.ids.eq1.model.store.product.Product

/**
 * Repository interface defining operations for product data management.
 */
interface ProductRepository {

    /**
     * Retrieves a product by its ID.
     */
    suspend fun getById(id: Int): Product?

    /**
     * Retrieves all products.
     */
    suspend fun getAll(): List<Product>

    /**
     * Adds a new product.
     */
    suspend fun add(product: Product): Int

    /**
     * Updates an existing product.
     */
    suspend fun update(product: Product): Boolean

    /**
     * Deletes a product by ID.
     */
    suspend fun delete(id: Int): Boolean

    /**
     * Deletes all products.
     */
    suspend fun deleteAll(): Boolean

    /**
     * Retrieves products below minimum stock levels.
     */
    suspend fun getBelowMinStock(): List<Product>
}