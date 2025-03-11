package mx.unam.fciencias.ids.eq1.model.store

import kotlinx.serialization.Serializable

@Serializable
data class Store(
    val id : Int,
    val name: String,
    val address: String,
    val createdAt: Long,
    val owner : Int
)