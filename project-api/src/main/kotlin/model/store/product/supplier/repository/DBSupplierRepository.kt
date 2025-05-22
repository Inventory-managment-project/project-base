package mx.unam.fciencias.ids.eq1.model.store.product.supplier.repository

import mx.unam.fciencias.ids.eq1.db.store.StoreTable
import mx.unam.fciencias.ids.eq1.db.store.product.supplier.SupplierDAO
import mx.unam.fciencias.ids.eq1.db.store.product.supplier.SupplierDAO.Companion.supplierDaoToModel
import mx.unam.fciencias.ids.eq1.db.store.product.supplier.SupplierTable
import mx.unam.fciencias.ids.eq1.db.utils.suspendTransaction
import mx.unam.fciencias.ids.eq1.model.store.product.supplier.Supplier
import mx.unam.fciencias.ids.eq1.model.store.product.supplier.CreateSupplierRequest
import mx.unam.fciencias.ids.eq1.model.store.product.supplier.UpdateSupplierRequest
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.annotation.Factory
import java.time.Instant

/**
 * Database-backed implementation of [SupplierRepository] using Exposed.
 * This repository handles all supplier-related database operations.
 */
@Factory
class DBSupplierRepository (
    private val database: Database,
    private val storeId: Int
) : SupplierRepository {

    /**
     * Initializes the database schema for supplier table if it doesn't exist
     */
    init {
        transaction(database) {
            SchemaUtils.create(SupplierTable)
        }
    }

    /**
     * Retrieves a supplier by its ID
     * @param id The supplier ID to look for
     * @return The supplier if found, null otherwise
     */
    override suspend fun getById(id: Int): Supplier? = suspendTransaction(database) {
        SupplierDAO
            .find { (SupplierTable.storeId eq storeId) and (SupplierTable.supplierId eq id) }
            .limit(1)
            .map(::supplierDaoToModel)
            .firstOrNull()
    }

    /**
     * Retrieves all suppliers for the current store
     * @return A list of all suppliers
     */
    override suspend fun getAll(): List<Supplier> = suspendTransaction(database) {
        SupplierDAO
            .find { SupplierTable.storeId eq storeId }
            .map(::supplierDaoToModel)
    }

    /**
     * Adds a new supplier to the store
     * @param supplier The supplier data to add
     * @param storeId The store ID to associate the supplier with
     * @return The ID of the newly created supplier, or -1 if creation failed
     */
    override suspend fun add(supplier: CreateSupplierRequest, storeId: Int): Int = suspendTransaction(database) {
        try {
            val supplierDao = SupplierDAO.new {
                // Generate a new ID by finding the maximum existing ID and incrementing it
                supplierId = (SupplierDAO.find { SupplierTable.storeId eq storeId }
                    .maxOfOrNull { it.supplierId } ?: 0) + 1
                name = supplier.name
                contactName = supplier.contactName
                contactPhone = supplier.contactPhone
                email = supplier.email
                address = supplier.address
                this.storeId = EntityID(storeId, StoreTable)
                createdAt = Instant.now()
            }
            supplierDao.supplierId
        } catch (e: Exception) {
            -1
        }
    }

    /**
     * Updates an existing supplier
     * @param id The ID of the supplier to update
     * @param supplier The new supplier data
     * @return true if the update was successful, false otherwise
     */
    override suspend fun update(id: Int, supplier: UpdateSupplierRequest): Boolean = suspendTransaction(database) {
        SupplierDAO
            .find { (SupplierTable.storeId eq storeId) and (SupplierTable.supplierId eq id) }
            .singleOrNull()
            ?.apply {
                name = supplier.name
                if (supplier.contactName != null) contactName = supplier.contactName
                contactPhone = supplier.contactPhone
                if (supplier.email != null) email = supplier.email
                if (supplier.address != null) address = supplier.address
            } != null
    }

    /**
     * Deletes a supplier by ID
     * @param id The ID of the supplier to delete
     * @return true if the supplier was successfully deleted, false otherwise
     */
    override suspend fun delete(id: Int): Boolean = suspendTransaction(database) {
        SupplierDAO
            .find { (SupplierTable.storeId eq storeId) and (SupplierTable.supplierId eq id) }
            .singleOrNull()
            ?.apply {
                delete()
            } != null
    }

    /**
     * Searches for suppliers by name (partial match)
     * @param name The name or part of the name to search for
     * @return A list of suppliers matching the search criteria
     */
    override suspend fun getByName(name: String): List<Supplier> = suspendTransaction(database) {
        SupplierDAO
            .find { (SupplierTable.storeId eq storeId) and (SupplierTable.name like "%$name%") }
            .map(::supplierDaoToModel)
    }

    /**
     * Searches for suppliers by contact name (partial match)
     * @param contact The contact name or part of it to search for
     * @return A list of suppliers matching the search criteria
     */
    override suspend fun getByContact(contact: String): List<Supplier> = suspendTransaction(database) {
        SupplierDAO
            .find { (SupplierTable.storeId eq storeId) and (SupplierTable.contactName like "%$contact%") }
            .map(::supplierDaoToModel)
    }

    /**
     * Checks if a supplier with the given ID exists
     * @param id The supplier ID to check
     * @return true if the supplier exists, false otherwise
     */
    override suspend fun existsById(id: Int): Boolean = suspendTransaction(database) {
        !SupplierDAO
            .find { (SupplierTable.storeId eq storeId) and (SupplierTable.supplierId eq id) }
            .empty()
    }

    /**
     * Retrieves all suppliers for a specific store
     * @param storeId The store ID to retrieve suppliers for
     * @return A list of suppliers for the specified store
     */
    override suspend fun getByStoreId(storeId: Int): List<Supplier> = suspendTransaction(database) {
        SupplierDAO
            .find { SupplierTable.storeId eq storeId }
            .map(::supplierDaoToModel)
    }
}