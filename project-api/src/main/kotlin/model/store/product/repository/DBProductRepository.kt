package mx.unam.fciencias.ids.eq1.model.store.product.repository

import mx.unam.fciencias.ids.eq1.db.store.product.ProductDAO
import mx.unam.fciencias.ids.eq1.db.store.product.ProductDAO.Companion.productDaoToModel
import mx.unam.fciencias.ids.eq1.db.store.product.ProductTable
import mx.unam.fciencias.ids.eq1.db.utils.suspendTransaction
import mx.unam.fciencias.ids.eq1.model.store.product.Product
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.annotation.Factory


@Factory
class DBProductRepository(
    private val database: Database,
    private val storeId: Int
) : ProductRepository {

    init {
        transaction(database) {
            SchemaUtils.create(ProductTable)
        }
    }

    override suspend fun getById(id: Int): Product? = suspendTransaction(database) {
        ProductDAO
            .find { (ProductTable.id eq id) and (ProductTable.storeId eq id) }
            .firstOrNull()
            ?.let { productDaoToModel(it) }
    }

    override suspend fun getAll(): List<Product> = suspendTransaction(database) {
        ProductDAO
            .find { ProductTable.storeId eq storeId }
            .map(::productDaoToModel)
    }

    override suspend fun add(product: Product): Int = suspendTransaction(database) {
        val productDao = ProductDAO.new {
            productId = product.id
            name = product.name
            description = product.description
            price = product.price
            stock = product.stock
            wholesalePrice = product.wholesalePrice
            retailPrice = product.retailPrice
            minAllowStock = product.minAllowStock
            this.storeId = EntityID(this@DBProductRepository.storeId, ProductTable)
        }
        productDao.id.value
    }

    override suspend fun update(product: Product): Boolean = suspendTransaction(database) {
        val productDao = ProductDAO
            .find { (ProductTable.id eq product.id) and (ProductTable.storeId eq storeId) }
            .firstOrNull() ?: return@suspendTransaction false

        productDao.name = product.name
        productDao.description = product.description
        productDao.price = product.price
        productDao.stock = product.stock

        true
    }

    override suspend fun delete(id: Int): Boolean = suspendTransaction(database) {
        val productDao = ProductDAO
            .find { (ProductTable.id eq id) and (ProductTable.storeId eq storeId) }
            .firstOrNull() ?: return@suspendTransaction false

        productDao.delete()
        true
    }

    override suspend fun deleteAll(): Boolean = suspendTransaction(database) {
        ProductDAO
            .find { ProductTable.storeId eq storeId }
            .forEach { it.delete() }
        true
    }

    override suspend fun getBelowMinStock(): List<Product> {
        TODO("Not yet implemented")
    }
}