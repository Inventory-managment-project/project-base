package mx.unam.fciencias.ids.eq1.model.user

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

    override fun delete(id: Int) {
        TODO("Not yet implemented")
    }

    override fun deleteAll() {
        TODO("Not yet implemented")
    }

    override fun count(): Int {
        TODO("Not yet implemented")
    }

    override fun add(user: User) {
        TODO("Not yet implemented")
    }

    override fun updateEmail(user: User, email: String) {
        TODO("Not yet implemented")
    }
}


