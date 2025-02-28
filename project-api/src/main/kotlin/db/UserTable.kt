package mx.unam.fciencias.ids.eq1.db

import mx.unam.fciencias.ids.eq1.db.user.UserDAO
import mx.unam.fciencias.ids.eq1.model.user.User
import org.jetbrains.exposed.dao.id.IntIdTable

object UserTable : IntIdTable("user") {
    val name = varchar("name", 255)
    val email = varchar("email", 255)
    val password = varchar("hashpasswordsalted", 255)
    val salt = varchar("salt", 128)
    val createdAt = varchar("createdat", 50)
}




