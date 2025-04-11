package mx.unam.fciencias.ids.eq1.service.store.product.supplier


import mx.unam.fciencias.ids.eq1.model.store.product.supplier.Supplier
import mx.unam.fciencias.ids.eq1.model.store.product.supplier.CreateSupplierRequest
import mx.unam.fciencias.ids.eq1.model.store.product.supplier.UpdateSupplierRequest

interface SupplierService{
    /**
     * Retrieves all suppliers.
     */
    suspend fun getAllSuppliers(): List<Supplier>

    /**
     * Retrieves a supplier by its ID.
     */
    suspend fun getSupplierById(id: Int): Supplier?

    /**
     * Creates a new supplier from the request data.
     */
    suspend fun createSupplier(request: CreateSupplierRequest): Int

    /**
     * Updates an existing supplier using the request data.
     */
    suspend fun updateSupplier(request: UpdateSupplierRequest): Boolean

    /**
     * Deletes a supplier by ID.
     */
    suspend fun deleteSupplier(id: Int): Boolean
}
