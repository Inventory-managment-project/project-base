package mx.unam.fciencias.ids.eq1.model.user.repository

import mx.unam.fciencias.ids.eq1.db.user.UserDAO
import mx.unam.fciencias.ids.eq1.db.user.UserDAO.Companion.userDaoToModel
import mx.unam.fciencias.ids.eq1.db.user.UserTable
import mx.unam.fciencias.ids.eq1.db.utils.suspendTransaction
import mx.unam.fciencias.ids.eq1.model.user.User
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.annotation.Single

/**
 * Database-backed implementation of [UserRepository] using Exposed.
 */
@Single
class DBUserRepository(
    private val database: Database
) : UserRepository {

    init {
        transaction(database) {
            SchemaUtils.create(UserTable)
        }
    }

    override suspend fun getById(id: Int): User? =suspendTransaction(database) {
        UserDAO
            .find { UserTable.id eq id }
            .limit(1)
            .map(::userDaoToModel)
            .firstOrNull()
    }

    override suspend fun getByName(name: String): User? = suspendTransaction(database) {
        UserDAO
            .find { UserTable.name eq name }
            .limit(1)
            .map(::userDaoToModel)
            .firstOrNull()
    }

    override suspend fun getAll(): List<User> =suspendTransaction(database) {
        UserDAO
            .all()
            .map(::userDaoToModel)
    }

    override suspend fun getFiltered(filter: (User) -> Boolean): List<User> =suspendTransaction(database) {
        UserDAO
            .all()
            .map(::userDaoToModel)
            .filter(filter)
    }

    override suspend fun delete(id: Int): Boolean = suspendTransaction(database) {
        UserDAO[id].delete()
        return@suspendTransaction UserDAO.findById(id) != null
    }

    override suspend fun deleteAll(): Boolean = suspendTransaction(database) {
        UserDAO.table.deleteAll()
        return@suspendTransaction UserDAO.count() == 0L
    }

    override suspend fun count(): Long = suspendTransaction(database) {
        return@suspendTransaction UserDAO.table.selectAll().count()
    }

    override suspend fun add(user: User): Boolean = suspendTransaction(database) {
        UserDAO.new {
            name = user.name
            email = user.email
            password = user.hashedPassword
            salt = user.salt
        }
        return@suspendTransaction true
    }

    override suspend fun updateEmail(userId: Int, email: String): Boolean =suspendTransaction(database) {
        UserDAO[userId].email = email
        return@suspendTransaction true
    }
}