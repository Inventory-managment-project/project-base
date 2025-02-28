package mx.unam.fciencias.ids.eq1.db.user

import mx.unam.fciencias.ids.eq1.model.user.User
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class UserDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserDAO>(UserTable) {
        fun userDaoToModel(dao: UserDAO) = User(
            dao.id.value,
            dao.name,
            dao.email,
            dao.password,
            dao.salt,
            dao.createdAt
        )
    }

    var name by UserTable.name
    var email by UserTable.email
    var password by UserTable.password
    var salt by UserTable.salt
    var createdAt by UserTable.createdAt

}