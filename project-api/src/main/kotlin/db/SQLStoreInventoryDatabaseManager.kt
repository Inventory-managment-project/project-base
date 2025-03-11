package mx.unam.fciencias.ids.eq1.db

import mx.unam.fciencias.ids.eq1.model.store.CreateStoreRequest
import mx.unam.fciencias.ids.eq1.model.store.repository.StoreRepository
import mx.unam.fciencias.ids.eq1.model.user.User
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
    private val storeRepository: StoreRepository
) : StoreInventoryDatabaseManager {

    private val databases = ConcurrentHashMap<String, Database>()

    override fun getStoreDatabase(storeId: String): Database {
        return databases.getOrPut(storeId) {
            connectToDatabase(storeId)
        }
    }

    override suspend fun createStoreDatabase(createStoreRequest: CreateStoreRequest, user: User): Database {
        val storeId = storeRepository.add(createStoreRequest, user)
        val databaseName = "store_$storeId"

        getConnection("${baseConnectionUrl}postgres", dbUser, dbPassword).use { connection ->
            connection.createStatement().execute("CREATE DATABASE $databaseName")
        }

        return connectToDatabase(storeId.toString())
    }

    private fun connectToDatabase(storeId: String): Database {
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