package mx.unam.fciencias.ids.eq1.db.utils

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

suspend fun <T> suspendTransaction(db : Database,block: Transaction.() -> T): T =
    newSuspendedTransaction(Dispatchers.IO,db = db, statement = block)