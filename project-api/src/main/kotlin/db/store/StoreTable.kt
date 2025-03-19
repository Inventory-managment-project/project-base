package mx.unam.fciencias.ids.eq1.db.store

import mx.unam.fciencias.ids.eq1.db.user.UserTable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.CurrentTimestamp
import org.jetbrains.exposed.sql.javatime.timestamp

object StoreTable : IntIdTable("stores") {
    val name = varchar("name", 255).uniqueIndex()
    val owner = reference("owner_id", UserTable)
    val address = varchar("address", 255)
    val created = timestamp("created").defaultExpression(CurrentTimestamp)
}