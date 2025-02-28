package mx.unam.fciencias.ids.eq1.model.user

interface UserRepository {
    suspend fun getById(id: Int): User?
    suspend fun getByName(name: String): User?
    suspend fun getAll(): List<User>
    suspend fun getFiltered(filter : (User) -> Boolean) : List<User>

    suspend fun delete(id: Int) : Boolean

    suspend fun deleteAll() : Boolean

    suspend fun count(): Long

    suspend fun add(user: User) : Boolean

    suspend fun updateEmail(userId : Int, email: String) : Boolean
}