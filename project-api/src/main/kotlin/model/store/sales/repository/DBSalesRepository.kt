package model.store.sales.repository

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


    override suspend fun getById(id: Int): Sales? = suspendTransaction(database) {
        SalesDAO.find { (SalesTable.storeId eq storeID) and (SalesTable.salesId eq id) }
            .firstOrNull()
            ?.let { salesDaoToModel(it) }
    }

    override suspend fun getAll(): List<Sales> = suspendTransaction(database) {
        SalesDAO.find { SalesTable.storeId eq storeID }
            .map { salesDaoToModel(it) }
    }

    override suspend fun add(sale: Sales): Int = suspendTransaction(database) {
        try {
            val nextSalesId = (SalesDAO.find { SalesTable.storeId eq storeID }
                .maxOfOrNull { it.salesId } ?: 0) + 1

            // Calculate total based on product prices and quantities
            val calculatedTotal = sale.products.fold(BigDecimal.ZERO) { acc, (productId, quantity) ->
                val product = ProductDAO.find {
                    (ProductTable.productId eq productId) and (ProductTable.storeId eq storeID)
                }.firstOrNull()
                val price = product?.prices?.firstOrNull()?.retailPrice ?: BigDecimal.ZERO
                acc + (price * quantity)
            }

            val saleId = SalesTable.insertAndGetId { builder ->
                builder[SalesTable.storeId] = EntityID(storeID, StoreTable)
                builder[SalesTable.salesId] = nextSalesId
                builder[SalesTable.total] = calculatedTotal
                builder[SalesTable.paymentMethod] = when (sale.paymentmethod.lowercase()) {
                    "cash" -> PAYMENTMETHOD.CASH
                    "card", "credit card", "debit card", "creditcard", "debitcard" -> PAYMENTMETHOD.CARD
                    else -> PAYMENTMETHOD.CASH // Default fallback
                }
            }

            // Insert sale details and update product stock
            sale.products.forEach { (productId, quantity) ->
                val product = ProductDAO.find {
                    (ProductTable.storeId eq storeID) and (ProductTable.productId eq productId)
                }.firstOrNull()

                if (product != null) {
                    SalesDetailsTable.insert { salesDetails ->
                        salesDetails[SalesDetailsTable.salesId] = saleId
                        salesDetails[SalesDetailsTable.productId] = product.id
                        salesDetails[SalesDetailsTable.quantity] = quantity
                    }

                    // Update product stock
                    ProductDAO.findSingleByAndUpdate(
                        (ProductTable.storeId eq storeID) and (ProductTable.productId eq productId)
                    ) {
                        it.stock -= quantity
                    }
                }
            }

            nextSalesId
        } catch (e: Exception) {
            -1
        }
    }

    override suspend fun update(sale: Sales): Boolean = suspendTransaction(database) {
        try {
            // Find the existing sale
            val existingSale = SalesDAO.find {
                (SalesTable.storeId eq storeID) and (SalesTable.salesId eq sale.id)
            }.firstOrNull() ?: return@suspendTransaction false

            // Calculate new total
            val newTotal = sale.products.fold(BigDecimal.ZERO) { acc, (productId, quantity) ->
                val product = ProductDAO.find {
                    (ProductTable.productId eq productId) and (ProductTable.storeId eq storeID)
                }.firstOrNull()
                val price = product?.prices?.firstOrNull()?.retailPrice ?: BigDecimal.ZERO
                acc + (price * quantity)
            }

            // Update the sale record
            SalesTable.update({
                (SalesTable.storeId eq storeID) and (SalesTable.salesId eq sale.id)
            }) {
                it[total] = newTotal
                it[paymentMethod] = when (sale.paymentmethod.lowercase()) {
                    "cash" -> PAYMENTMETHOD.CASH
                    "card", "credit card", "debit card", "creditcard", "debitcard" -> PAYMENTMETHOD.CARD
                    else -> PAYMENTMETHOD.CASH
                }
            }

            // Delete existing sale details
            SalesDetailsTable.deleteWhere {
                salesId eq existingSale.id
            }

            // Insert new sale details and update stock
            sale.products.forEach { (productId, quantity) ->
                val product = ProductDAO.find {
                    (ProductTable.storeId eq storeID) and (ProductTable.productId eq productId)
                }.firstOrNull()

                if (product != null) {
                    SalesDetailsTable.insert { salesDetails ->
                        salesDetails[SalesDetailsTable.salesId] = existingSale.id
                        salesDetails[SalesDetailsTable.productId] = product.id
                        salesDetails[SalesDetailsTable.quantity] = quantity
                    }
                }
            }

            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun delete(id: Int): Boolean = suspendTransaction(database) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAll(): Boolean = suspendTransaction(database) {
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