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
                builder[total] = sale.total
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

    override suspend fun update(sale: Sale): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun delete(id: Int): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAll(): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun getByPaymentMethod(paymentMethod: PAYMENTMETHOD): List<Sale> {
        TODO("Not yet implemented")
    }

    override suspend fun getByDateRange(startDate: Long, endDate: Long): List<Sale> {
        TODO("Not yet implemented")
    }

    override suspend fun getTotalRevenue(startDate: Long, endDate: Long): BigDecimal {
        TODO("Not yet implemented")
    }

    override suspend fun getSalesByProductId(productId: Int): List<Sale> {
        TODO("Not yet implemented")
    }
}