package mx.unam.fciencias.ids.eq1.db

import org.jetbrains.exposed.sql.Database
import org.koin.core.annotation.Single
import java.sql.DriverManager.getConnection
import java.util.concurrent.ConcurrentHashMap

@Single
class SQLStoreInventoryDatabaseManager(
    private val baseConnectionUrl: String,
    private val dbUser: String,
    private val dbPassword: String,
    private val driver: String,
) : StoreInventoryDatabaseManager {

    private val databases = ConcurrentHashMap<Int, Database>()

    override fun getStoreDatabase(storeId: Int): Database {
        return databases.getOrPut(storeId) {
            connectToDatabase(storeId)
        }
    }

    override suspend fun createStoreDatabase(storeId: Int): Database {
        val databaseName = "store_$storeId"

        getConnection("${baseConnectionUrl}postgres", dbUser, dbPassword).use { connection ->
            connection.createStatement().execute("CREATE DATABASE $databaseName")
        }

        return connectToDatabase(storeId)
    }

    private fun connectToDatabase(storeId: Int): Database {
        val databaseName = "store_$storeId"
        return Database.connect(
            url = "$baseConnectionUrl/$databaseName",
            driver = driver,
            user = dbUser,
            password = dbPassword
        ).also {
            databases[storeId] = it
        }
    }
}