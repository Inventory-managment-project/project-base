package mx.unam.fciencias.ids.eq1.model.user

interface UserRepository {
    fun getById(id: Int): User?
    fun getByName(name: String): User?
    fun getAll(): List<User>
    fun getFiltered(filter : (User) -> Boolean) : List<User>

    fun delete(id: Int)

    fun deleteAll()

    fun count(): Int

    fun add(user: User)

    fun updateEmail(user: User, email: String)
}