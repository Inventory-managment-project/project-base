package mx.unam.fciencias.ids.eq1.model.store.sales.repository

import io.ktor.server.sessions.*
import kotlinx.css.data
import mx.unam.fciencias.ids.eq1.db.store.sales.PAYMENTMETHOD
import mx.unam.fciencias.ids.eq1.db.store.sales.SalesDAO
import mx.unam.fciencias.ids.eq1.db.store.sales.SalesDetailsTable
import mx.unam.fciencias.ids.eq1.db.store.sales.SalesTable
import mx.unam.fciencias.ids.eq1.db.utils.suspendTransaction
import mx.unam.fciencias.ids.eq1.model.store.sales.Sales
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.annotation.Factory
import java.math.BigDecimal

@Factory

class DBSalesRepository (
    private val database: Database,
    private val storeId: Int
) : SalesRepository {

    init {
        transaction(database){
            SchemaUtils.create(SalesTable, SalesDetailsTable)
        }
    }


    override suspend fun getById(id: Int): Sales? = suspendTransaction(database) {
        SalesDAO.find { SalesTable.storeId eq storeId}
            .find { SalesTable.salesId.equals(id) }
            ?.let { SalesDAO.salesDaoToModel(it) }

    }

    override suspend fun getAll(): List<Sales>  = suspendTransaction(database){
        TODO("Not yet implemented")
    }

    override suspend fun add(sales: Sales): Int {
        TODO("Not yet implemented")
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