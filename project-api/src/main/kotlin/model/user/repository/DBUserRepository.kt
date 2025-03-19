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
        val userToDelete = UserDAO.findById(id)
        if (userToDelete != null) {
            userToDelete.delete()
            return@suspendTransaction true
        } else {
            return@suspendTransaction false
        }

    }

    override suspend fun deleteAll(): Boolean = suspendTransaction(database) {
        if (UserDAO.all().count() > 0) {
            UserDAO.table.deleteAll()
            return@suspendTransaction UserDAO.count() == 0L
        } else return@suspendTransaction false

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
        val updatedUser = UserDAO.findById(userId)
        if (updatedUser != null) {
            updatedUser.email = email
            return@suspendTransaction true
        } else {
            return@suspendTransaction false
        }
    }

    override suspend fun getByEmail(email: String): User? {
        TODO("Not yet implemented")
    }

    override suspend fun updatePassword(userId: Int, hashedPassword: String, salt: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun existsById(id: Int): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun existsByEmail(email: String): Boolean {
        TODO("Not yet implemented")
    }
}