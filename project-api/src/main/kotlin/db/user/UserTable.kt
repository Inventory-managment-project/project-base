package mx.unam.fciencias.ids.eq1.db.user

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.*

object UserTable : IntIdTable("user") {
    val name = varchar("name", 255)
    val email = varchar("email", 255)
    val password = varchar("hashpasswordsalted", 255)
    val salt = varchar("salt", 128)
    val createdAt = timestamp("createdat").defaultExpression(CurrentTimestamp)
}
