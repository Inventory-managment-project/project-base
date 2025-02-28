package mx.unam.fciencias.ids.eq1.model.user

import mx.unam.fciencias.ids.eq1.db.UserDAO
import mx.unam.fciencias.ids.eq1.db.UserTable
import mx.unam.fciencias.ids.eq1.db.suspendTransaction
import mx.unam.fciencias.ids.eq1.db.userDaoToModel
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.annotation.Single


@Single
class PostgresqlUserRepository(private val database: Database) : UserRepository {

    init {
        transaction(database) {
            SchemaUtils.create(UserTable)
        }
    }

    override suspend fun getById(id: Int): User? = suspendTransaction {
        UserDAO
            .find { UserTable.id eq id }
            .limit(1)
            .map(::userDaoToModel)
            .firstOrNull()
    }

    override suspend fun getByName(name: String): User? = suspendTransaction {
        UserDAO
            .find { UserTable.name eq name }
            .limit(1)
            .map(::userDaoToModel)
            .firstOrNull()
    }

    override suspend fun getAll(): List<User> = suspendTransaction {
        UserDAO
            .all()
            .map(::userDaoToModel)
    }

    override suspend fun getFiltered(filter: (User) -> Boolean): List<User>  = suspendTransaction {
        UserDAO
            .all()
            .map(::userDaoToModel)
            .filter(filter)
    }

    override suspend fun delete(id: Int): Boolean {
        UserDAO[id].delete()
        return UserDAO.findById(id) != null
    }

    override suspend fun deleteAll(): Boolean {
        UserDAO.table.deleteAll()
        return UserDAO.count() == 0L
    }

    override suspend fun count(): Long {
        var i = 0L
        transaction(database) {
            i = UserTable.selectAll().count()
        }
        return i
    }

    override suspend fun add(user: User): Boolean {
        UserDAO.new {
            name = user.name
            email = user.email
            password = user.password
            salt = user.salt
            createdAt = user.createdAt
        }
        return true
    }

    override suspend fun updateEmail(userId: Int, email: String): Boolean {
        UserDAO[userId].email = email
        return true
    }
}

