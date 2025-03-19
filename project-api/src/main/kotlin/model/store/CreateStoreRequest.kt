package mx.unam.fciencias.ids.eq1.model.store

import kotlinx.serialization.Serializable

@Serializable
data class CreateStoreRequest(
    val name: String,
    val address: String
)