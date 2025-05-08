package mx.unam.fciencias.ids.eq1.model.store.product.repository

import mx.unam.fciencias.ids.eq1.db.store.product.ProductDAO
import mx.unam.fciencias.ids.eq1.db.store.product.ProductDAO.Companion.productDaoToModel
import mx.unam.fciencias.ids.eq1.db.store.product.ProductPriceDAO
import mx.unam.fciencias.ids.eq1.db.store.product.ProductPriceTable
import mx.unam.fciencias.ids.eq1.db.store.product.ProductTable
import mx.unam.fciencias.ids.eq1.db.utils.suspendTransaction
import mx.unam.fciencias.ids.eq1.model.store.product.Product
import org.jetbrains.exposed.dao.id.CompositeID
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.annotation.Factory
import java.time.Instant


@Factory
class DBProductRepository(
    private val database: Database,
    private val storeID: Int
) : ProductRepository {

    init {
        transaction(database) {
            SchemaUtils.create(ProductTable, ProductPriceTable)
        }
    }

    override suspend fun getById(id: Int): Product? = suspendTransaction(database) {
        ProductDAO
            .find { (ProductTable.productId eq id) and (ProductTable.storeId eq storeID) }
            .firstOrNull()
            ?.let { productDaoToModel(it) }
    }

    override suspend fun getAll(): List<Product> = suspendTransaction(database) {
        ProductDAO
            .find { (ProductTable.storeId eq storeID) and (ProductTable.active) }
            .map(::productDaoToModel)
    }

    override suspend fun add(product: Product): Int = suspendTransaction(database) {
        if (!ProductDAO.find {
            (ProductTable.storeId eq storeID) and (ProductTable.barcode eq product.barcode)
        }.empty()) return@suspendTransaction -1
        val productDao = ProductDAO.new {
            productId = (ProductDAO.find { ProductTable.storeId eq storeID }
                .maxOfOrNull { it.productId } ?: 0) + 1
            name = product.name
            description = product.description
            stock = product.stock
            barcode = product.barcode
            minAllowStock = product.minAllowStock
            this.storeId = EntityID(this@DBProductRepository.storeID, ProductTable)
        }
        val productPrice = CompositeID {
            it[ProductPriceTable.productId] = productDao.id
            it[ProductPriceTable.timestamp] = Instant.now()
        }
        ProductPriceDAO.new(productPrice) {
            productId = productDao.id
            price = product.price
            retailPrice = product.retailPrice
            wholesalePrice = product.wholesalePrice
        }
        productDao.productId
    }

    override suspend fun update(product: Product): Boolean = suspendTransaction(database) {
        val prodId = ProductDAO.find { (ProductTable.storeId eq storeID) and (ProductTable.productId eq product.id) }
            .firstOrNull()?.id ?: return@suspendTransaction false
        ProductDAO.findByIdAndUpdate(prodId.value) {
            it.name = product.name
            it.description = product.description
            it.stock = product.stock
        }?.let {
            val productPrice = CompositeID {
                it[ProductPriceTable.productId] = product.id
                it[ProductPriceTable.timestamp] = Instant.now()
            }
            ProductPriceDAO.new(productPrice) {
                productId = it.id
                price = product.price
                retailPrice = product.retailPrice
                wholesalePrice = product.wholesalePrice
            }
            return@suspendTransaction true
        }
        false
    }

    override suspend fun delete(id: Int): Boolean = suspendTransaction(database) {
        val productDao = ProductDAO
            .find { (ProductTable.productId eq id) and (ProductTable.storeId eq storeID) }
            .firstOrNull() ?: return@suspendTransaction false
        productDao.active = false
        true
    }

    override suspend fun deleteAll(): Boolean = suspendTransaction(database) {
        ProductDAO
            .find { ProductTable.storeId eq storeID }
            .forEach { it.active = false }
        true
    }

    override suspend fun getBelowMinStock(): List<Product> {
        TODO("Not yet implemented")
    }

    override suspend fun getByBarcode(barcode: String): Product? = suspendTransaction(database) {
     ProductDAO
          .find { (ProductTable.storeId eq storeID) and (ProductTable.barcode eq barcode) }
          .firstOrNull()
          ?.let { productDaoToModel(it) }
    }
}