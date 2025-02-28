package mx.unam.fciencias.ids.eq1.model.user

import mx.unam.fciencias.ids.eq1.db.user.UserDAO
import mx.unam.fciencias.ids.eq1.db.user.UserDAO.Companion.userDaoToModel
import mx.unam.fciencias.ids.eq1.db.user.UserTable
import mx.unam.fciencias.ids.eq1.db.utils.suspendTransaction
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.annotation.Single


@Single
class DBUserRepository(database: Database) : UserRepository {

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

    override suspend fun delete(id: Int): Boolean = suspendTransaction {
        UserDAO[id].delete()
        return@suspendTransaction UserDAO.findById(id) != null
    }

    override suspend fun deleteAll(): Boolean = suspendTransaction {
        UserDAO.table.deleteAll()
        return@suspendTransaction UserDAO.count() == 0L
    }

    override suspend fun count(): Long = suspendTransaction {
        return@suspendTransaction UserDAO.table.selectAll().count()
    }

    override suspend fun add(user: User): Boolean = suspendTransaction {
        UserDAO.new {
            name = user.name
            email = user.email
            password = user.password
            salt = user.salt
            createdAt = user.createdAt
        }
        return@suspendTransaction true
    }

    override suspend fun updateEmail(userId: Int, email: String): Boolean  = suspendTransaction {
        UserDAO[userId].email = email
        return@suspendTransaction true
    }
}

