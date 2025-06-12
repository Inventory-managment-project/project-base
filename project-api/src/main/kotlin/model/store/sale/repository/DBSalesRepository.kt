package model.store.sale.repository

import mx.unam.fciencias.ids.eq1.db.store.StoreTable
import mx.unam.fciencias.ids.eq1.db.store.product.ProductDAO
import mx.unam.fciencias.ids.eq1.db.store.product.ProductTable
import mx.unam.fciencias.ids.eq1.db.store.sales.PAYMENTMETHOD
import mx.unam.fciencias.ids.eq1.db.store.sales.SalesDAO
import mx.unam.fciencias.ids.eq1.db.store.sales.SalesDAO.Companion.salesDaoToModel
import mx.unam.fciencias.ids.eq1.db.store.sales.SalesDetailsTable
import mx.unam.fciencias.ids.eq1.db.store.sales.SalesTable
import mx.unam.fciencias.ids.eq1.db.utils.suspendTransaction
import mx.unam.fciencias.ids.eq1.model.store.sales.Sale
import mx.unam.fciencias.ids.eq1.model.store.sales.repository.SalesRepository
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.annotation.Factory
import java.math.BigDecimal
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.and

@Factory
class DBSalesRepository (
    private val database: Database,
    private val storeID: Int
) : SalesRepository {

    init {
        transaction(database){
            SchemaUtils.create(SalesTable, SalesDetailsTable)
        }
    }


    override suspend fun getById(id: Int): Sale? = suspendTransaction(database) {
        SalesDAO.find { (SalesTable.storeId eq storeID) and (SalesTable.salesId eq id) }
            .firstOrNull()
            ?.let { salesDaoToModel(it) }

    }

    override suspend fun getAll(): List<Sale>  = suspendTransaction(database){
        SalesDAO.all().map(::salesDaoToModel)
    }

    override suspend fun add(sale: Sale): Int = suspendTransaction(database) {
        try {
            val saleId = SalesTable.insertAndGetId { builder ->
                builder[storeId] = EntityID(storeID, StoreTable)
                builder[SalesDetailsTable.salesId] = (SalesDAO.find { storeId eq storeID }
                    .maxOfOrNull { it.salesId } ?: 0) + 1
                builder[total] = sale.products.fold(BigDecimal(0.0)) { acc, pair ->
                    acc + (ProductDAO.find { (ProductTable.productId eq pair.first) and (ProductTable.storeId eq storeID) }
                        .firstOrNull()
                        ?.prices?.firstOrNull()?.retailPrice ?: BigDecimal(0.0))
                }
                builder[paymentMethod] = sale.paymentmethod
            }
            sale.products.forEach { product ->
                val prodId = ProductDAO.find { (ProductTable.storeId eq storeID) and (ProductTable.productId eq product.first) }.firstOrNull()
                if (prodId == null) return@forEach
                SalesDetailsTable.insert { salesDetails ->
                    salesDetails[salesId] = saleId
                    salesDetails[productId] = prodId.id
                    salesDetails[quantity] = product.second
                }
                ProductDAO.findSingleByAndUpdate((ProductTable.storeId eq storeID) and (ProductTable.productId eq product.first)) { it.stock -= product.second }
            }
            SalesDAO.findById(saleId)?.salesId ?: -1
        } catch (e : Exception) { -1 }
    }

    override suspend fun update(sale: Sale): Boolean = suspendTransaction(database) {
        try {
            val existingSale = SalesDAO.find {
                (SalesTable.storeId eq storeID) and (SalesTable.salesId eq sale.id)
            }.firstOrNull() ?: return@suspendTransaction false

            // Restaurar stock de productos anteriores
            SalesDetailsTable.select { SalesDetailsTable.salesId eq existingSale.id }.forEach { row ->
                val productId = row[SalesDetailsTable.productId]
                val quantity = row[SalesDetailsTable.quantity]
                ProductDAO.findByIdAndUpdate(productId) { it.stock += quantity }
            }

            // Eliminar detalles anteriores
            SalesDetailsTable.deleteWhere { salesId eq existingSale.id }

            // Actualizar venta principal
            val newTotal = sale.products.fold(BigDecimal(0.0)) { acc, pair ->
                acc + (ProductDAO.find { (ProductTable.productId eq pair.first) and (ProductTable.storeId eq storeID) }
                    .firstOrNull()
                    ?.prices?.firstOrNull()?.retailPrice ?: BigDecimal(0.0)) * BigDecimal(pair.second)
            }

            SalesTable.update({ SalesTable.id eq existingSale.id }) {
                it[total] = newTotal
                it[paymentMethod] = sale.paymentmethod
            }

            // Agregar nuevos productos
            sale.products.forEach { product ->
                val prodDAO = ProductDAO.find { (ProductTable.storeId eq storeID) and (ProductTable.productId eq product.first) }.firstOrNull()
                if (prodDAO == null) return@forEach

                SalesDetailsTable.insert { salesDetails ->
                    salesDetails[salesId] = existingSale.id
                    salesDetails[productId] = prodDAO.id
                    salesDetails[quantity] = product.second
                }

                ProductDAO.findSingleByAndUpdate((ProductTable.storeId eq storeID) and (ProductTable.productId eq product.first)) {
                    it.stock -= product.second
                }
            }
            true
        } catch (e: Exception) { false }
    }

    override suspend fun delete(id: Int): Boolean = suspendTransaction(database) {
        try {
            val sale = SalesDAO.find {
                (SalesTable.storeId eq storeID) and (SalesTable.salesId eq id)
            }.firstOrNull() ?: return@suspendTransaction false

            // Restaurar stock
            SalesDetailsTable.select { SalesDetailsTable.salesId eq sale.id }.forEach { row ->
                val productId = row[SalesDetailsTable.productId]
                val quantity = row[SalesDetailsTable.quantity]
                ProductDAO.findByIdAndUpdate(productId) { it.stock += quantity }
            }

            // Eliminar detalles y venta
            SalesDetailsTable.deleteWhere { salesId eq sale.id }
            SalesTable.deleteWhere { id eq sale.id }
            true
        } catch (e: Exception) { false }
    }

    override suspend fun deleteAll(): Boolean = suspendTransaction(database) {
        try {
            // Restaurar todo el stock
            SalesTable
                .join(SalesDetailsTable, JoinType.INNER, SalesTable.id, SalesDetailsTable.salesId)
                .select { SalesTable.storeId eq storeID }
                .forEach { row ->
                    val productId = row[SalesDetailsTable.productId]
                    val quantity = row[SalesDetailsTable.quantity]
                    ProductDAO.findByIdAndUpdate(productId) { it.stock += quantity }
                }

            // Eliminar todas las ventas de la tienda
            val saleIds = SalesDAO.find { SalesTable.storeId eq storeID }.map { it.id }
            SalesDetailsTable.deleteWhere { salesId inList saleIds }
            SalesTable.deleteWhere { storeId eq storeID }
            true
        } catch (e: Exception) { false }
    }

    override suspend fun getByPaymentMethod(paymentMethod: PAYMENTMETHOD): List<Sale> = suspendTransaction(database) {
        SalesDAO.find {
            (SalesTable.storeId eq storeID) and (SalesTable.paymentMethod eq paymentMethod)
        }.map(::salesDaoToModel)
    }

    override suspend fun getByDateRange(startDate: Long, endDate: Long): List<Sale> = suspendTransaction(database) {
        SalesDAO.find {
            (SalesTable.storeId eq storeID) and
                    (SalesTable.createdAt.between(startDate, endDate))
        }.map(::salesDaoToModel)
    }

    override suspend fun getTotalRevenue(startDate: Long, endDate: Long): BigDecimal = suspendTransaction(database) {
        SalesTable
            .select {
                (SalesTable.storeId eq storeID) and
                        (SalesTable.createdAt.between(startDate, endDate))
            }
            .sumOf { it[SalesTable.total] }
    }

    override suspend fun getSalesByProductId(productId: Int): List<Sale> = suspendTransaction(database) {
        val productDAO = ProductDAO.find {
            (ProductTable.storeId eq storeID) and (ProductTable.productId eq productId)
        }.firstOrNull() ?: return@suspendTransaction emptyList()

        val saleIds = SalesDetailsTable
            .select { SalesDetailsTable.productId eq productDAO.id }
            .map { it[SalesDetailsTable.salesId] }

        SalesDAO.find { SalesTable.id inList saleIds }.map(::salesDaoToModel)
    }
}