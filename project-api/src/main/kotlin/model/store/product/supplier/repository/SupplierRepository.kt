package mx.unam.fciencias.ids.eq1.model.store.product.supplier.repository

import mx.unam.fciencias.ids.eq1.model.store.product.Product
import mx.unam.fciencias.ids.eq1.model.store.product.supplier.Supplier
import mx.unam.fciencias.ids.eq1.model.store.product.supplier.CreateSupplierRequest
import mx.unam.fciencias.ids.eq1.model.store.product.supplier.UpdateSupplierRequest

/**
 * Repository interface defining operations for supplier data management.
 */
interface SupplierRepository{
    /**
     * Retrieves a supplier by its ID.
     */
    suspend fun getById(id: Int): Supplier?

    /**
     * Retrieves all suppliers.
     */
    suspend fun getAll(): List<Supplier>

    /**
     * Adds a new supplier.
     */
    suspend fun add(supplier: CreateSupplierRequest): Int

    /**
     * Updates an existing supplier.
     */
    suspend fun update(id: Int, supplier: UpdateSupplierRequest): Boolean

    /**
     * Deletes a supplier by ID.
     */
    suspend fun delete(id: Int): Boolean

    /**
     * Retrieves suppliers by name.
     */
    suspend fun getByName(name: String): List<Supplier>

    /**
     * Retrieves suppliers by contact information.
     */
    suspend fun getByContact(contact: String): List<Supplier>

    /**
     * Checks if a supplier exists by ID.
     */
    suspend fun existsById(id: Int): Boolean

    /**
     * Retrieves suppliers by store ID.
     */
    suspend fun addProductSupply(supplierID: Int, productID: Int): Boolean
    suspend fun suppliesProducts(supplerID: Int, productID: Int): Boolean
    suspend fun getAllProductsSupplier(id: Int): List<Product>
    suspend fun removeProductSupply(supplierID: Int, productID: Int): Boolean
}