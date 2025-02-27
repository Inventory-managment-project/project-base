package mx.unam.fciencias.ids.eq1.model.user

interface UserRepository {
    fun getById(id: Int): User?
    fun getByName(name: String): User?
    fun getAll(): List<User>
    fun getFiltered(filter : (User) -> Boolean) : List<User>

    fun delete(id: Int) : Boolean

    fun deleteAll() : Boolean

    fun count(): Int

    fun add(user: User) : Boolean

    fun updateEmail(userId : Int, email: String) : Boolean
}