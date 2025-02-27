package mx.unam.fciencias.ids.eq1.model.user

import org.koin.core.annotation.Single

@Single
class PostgresqlUserRepository : UserRepository {
    override fun getById(id: Int): User? {
        TODO("Not yet implemented")
    }

    override fun getByName(name: String): User? {
        TODO("Not yet implemented")
    }

    override fun getAll(): List<User> {
        TODO("Not yet implemented")
    }

    override fun getFiltered(filter: (User) -> Boolean): List<User> {
        TODO("Not yet implemented")
    }

    override fun delete(id: Int) : Boolean {
        TODO("Not yet implemented")
    }

    override fun deleteAll()  :Boolean {
        TODO("Not yet implemented")
    }

    override fun count(): Int {
        TODO("Not yet implemented")
    }

    override fun add(user: User) : Boolean {
        TODO("Not yet implemented")
    }

    override fun updateEmail(userId: Int, email: String) : Boolean {
        TODO("Not yet implemented")
    }
}


