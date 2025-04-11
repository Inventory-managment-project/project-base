package mx.unam.fciencias.ids.eq1.model.store.product.supplier.repository

import mx.unam.fciencias.ids.eq1.db.store.product.supplier.SupplierDAO
import mx.unam.fciencias.ids.eq1.db.store.product.supplier.SupplierDAO.Companion.supplierDaoToModel
import mx.unam.fciencias.ids.eq1.db.store.product.supplier.SupplierTable
import mx.unam.fciencias.ids.eq1.db.utils.suspendTransaction
import mx.unam.fciencias.ids.eq1.model.store.product.supplier.Supplier
import mx.unam.fciencias.ids.eq1.model.store.product.supplier.CreateSupplierRequest
import mx.unam.fciencias.ids.eq1.model.store.product.supplier.UpdateSupplierRequest
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.annotation.Factory

@Factory
class DBSupplierRepository (
    private val database: Database,
    private val storeId: Int
) : SupplierRepository {

    init {
        transaction(database) {
            SchemaUtils.create(SupplierTable)
        }
    }

    override suspend fun getById(id: Int): Supplier? = suspendTransaction(database) {
        SupplierDAO
            .find { (SupplierTable.storeId eq storeId) and (SupplierTable.supplierId eq id) }
            .limit(1)
            .map(::supplierDaoToModel)
            .firstOrNull()
    }

    override suspend fun getAll(): List<Supplier> = suspendTransaction(database) {
        SupplierDAO
            .find { SupplierTable.storeId eq storeId }
            .map(::supplierDaoToModel)
    }

    override suspend fun add(supplier: CreateSupplierRequest, storeId: Int): Int = suspendTransaction(database) {
        TODO("Not yet implemented")
    }

    override suspend fun update(id: Int, supplier: UpdateSupplierRequest): Boolean = suspendTransaction(database) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(id: Int): Boolean = suspendTransaction(database) {
        TODO("Not yet implemented")
    }

    override suspend fun getByName(name: String): List<Supplier> = suspendTransaction(database) {
        SupplierDAO
            .find { (SupplierTable.storeId eq storeId) and (SupplierTable.name like "%$name%") }
            .map(::supplierDaoToModel)
    }

    override suspend fun getByContact(contact: String): List<Supplier> = suspendTransaction(database) {
        TODO("Not yet implemented")
    }

    override suspend fun existsById(id: Int): Boolean = suspendTransaction(database) {
        TODO("Not yet implemented")
    }

    override suspend fun getByStoreId(storeId: Int): List<Supplier> = suspendTransaction(database) {
        SupplierDAO
            .find { SupplierTable.storeId eq storeId }
            .map(::supplierDaoToModel)
    }
}