package mx.unam.fciencias.ids.eq1.model.store.product.repository

import mx.unam.fciencias.ids.eq1.db.StoreInventoryDatabaseManager
import mx.unam.fciencias.ids.eq1.db.store.product.ProductDAO
import mx.unam.fciencias.ids.eq1.db.store.product.ProductDAO.Companion.productDaoToModel
import mx.unam.fciencias.ids.eq1.db.store.product.ProductTable
import mx.unam.fciencias.ids.eq1.db.utils.suspendTransaction
import mx.unam.fciencias.ids.eq1.model.store.product.Product
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.annotation.Factory

@Factory
class DBProductRepository(
    databaseManager: StoreInventoryDatabaseManager,
    storeId : Int
) : ProductRepository {

    private val database = databaseManager.getStoreDatabase(storeId)

    init {
        transaction(database) {
            SchemaUtils.create(ProductTable)
        }
    }

    override suspend fun getById(id: Int): Product? = suspendTransaction(database) {
        ProductDAO
            .findById(id)
            ?.let(::productDaoToModel)
    }

    override suspend fun getAll(): List<Product> = suspendTransaction(database) {
        ProductDAO
            .all()
            .map(::productDaoToModel)
    }

    override suspend fun add(product: Product): Int = suspendTransaction(database) {
        val productDao = ProductDAO.new {
            name = product.name
            description = product.description
            price = product.price
            stock = product.stock
        }
        productDao.id.value
    }

    override suspend fun update(product: Product): Boolean = suspendTransaction(database) {
        val productDao = ProductDAO.findById(product.id) ?: return@suspendTransaction false

        productDao.name = product.name
        productDao.description = product.description
        productDao.price = product.price
        productDao.stock = product.stock

        true
    }

    override suspend fun delete(id: Int): Boolean = suspendTransaction(database) {
        val productDao = ProductDAO.findById(id) ?: return@suspendTransaction false
        productDao.delete()
        true
    }

    override suspend fun deleteAll(): Boolean = suspendTransaction(database){
        ProductDAO.all().forEach { it.delete() }
        true
    }
}