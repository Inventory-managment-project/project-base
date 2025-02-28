package mx.unam.fciencias.ids.eq1.db

import kotlinx.coroutines.Dispatchers
import mx.unam.fciencias.ids.eq1.model.user.User
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

object UserTable : IntIdTable("user") {
    val name = varchar("name", 255)
    val email = varchar("email", 255)
    val password = varchar("hashpasswordsalted", 255)
    val salt = varchar("salt", 128)
    val createdAt = varchar("createdat", 50)
}


fun userDaoToModel(dao: UserDAO) = User(
    dao.id.value,
    dao.name,
    dao.email,
    dao.password,
    dao.salt,
    dao.createdAt
)

class UserDAO(id : EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserDAO>(UserTable)

    var name by UserTable.name
    var email by UserTable.email
    var password by UserTable.password
    var salt by UserTable.salt
    var createdAt by UserTable.createdAt

}