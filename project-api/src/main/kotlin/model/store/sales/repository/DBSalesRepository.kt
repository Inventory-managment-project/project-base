package mx.unam.fciencias.ids.eq1.model.store.sales.repository

import mx.unam.fciencias.ids.eq1.db.store.StoreTable
import mx.unam.fciencias.ids.eq1.db.store.product.ProductDAO
import mx.unam.fciencias.ids.eq1.db.store.product.ProductTable
import mx.unam.fciencias.ids.eq1.db.store.sales.PAYMENTMETHOD
import mx.unam.fciencias.ids.eq1.db.store.sales.SalesDAO
import mx.unam.fciencias.ids.eq1.db.store.sales.SalesDAO.Companion.salesDaoToModel
import mx.unam.fciencias.ids.eq1.db.store.sales.SalesDetailsTable
import mx.unam.fciencias.ids.eq1.db.store.sales.SalesTable
import mx.unam.fciencias.ids.eq1.db.utils.suspendTransaction
import mx.unam.fciencias.ids.eq1.model.store.sales.Sales
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.annotation.Factory
import java.math.BigDecimal

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


    override suspend fun getById(id: Int): Sales? = suspendTransaction(database) {
        SalesDAO.find { (SalesTable.storeId eq storeID) and (SalesTable.salesId eq id) }
            .firstOrNull()
            ?.let { salesDaoToModel(it) }

    }

    override suspend fun getAll(): List<Sales>  = suspendTransaction(database){
        SalesDAO.all().map(::salesDaoToModel)
    }

    override suspend fun add(sales: Sales): Int = suspendTransaction(database) {
        val sale = SalesTable.insertAndGetId { builder ->
            builder[storeId] = EntityID(storeID, StoreTable)
            builder[SalesDetailsTable.salesId] = (SalesDAO.find { storeId eq storeID }
                .maxOfOrNull { it.salesId } ?: 0) + 1
            builder[total] = sales.products.fold(BigDecimal(0.0)) { acc, pair ->
                acc + (ProductDAO.find { (ProductTable.productId eq pair.first) and (ProductTable.storeId eq storeID) }
                    .firstOrNull()
                    ?.retailPrice ?: BigDecimal(0.0))
            }
            builder[paymentMethod] = sales.paymentmethod
        }
        sales.products.forEach { product ->
            SalesDetailsTable.insert { salesDetails ->
                salesDetails[salesId] = sale.value
                salesDetails[productId] = product.first
                salesDetails[quantity] = product.second
            }
            ProductDAO.findSingleByAndUpdate((ProductTable.storeId eq storeID) and (ProductTable.productId eq product.first)) { it.stock -= product.second.toInt() }
        }
        SalesDAO.findById(sale)?.salesId ?: -1
    }

    override suspend fun update(sales: Sales): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun delete(id: Int): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAll(): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun getByPaymentMethod(paymentMethod: PAYMENTMETHOD): List<Sales> {
        TODO("Not yet implemented")
    }

    override suspend fun getByDateRange(startDate: Long, endDate: Long): List<Sales> {
        TODO("Not yet implemented")
    }

    override suspend fun getTotalRevenue(startDate: Long, endDate: Long): BigDecimal {
        TODO("Not yet implemented")
    }

    override suspend fun getSalesByProductId(productId: Int): List<Sales> {
        TODO("Not yet implemented")
    }
}