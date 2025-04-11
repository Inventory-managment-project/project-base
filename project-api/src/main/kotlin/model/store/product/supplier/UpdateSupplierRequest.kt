package mx.unam.fciencias.ids.eq1.model.store.product.supplier

import kotlinx.serialization.Serializable

@Serializable
data class UpdateSupplierRequest(
    val id: Int,
    val name: String,
    val contactName: String,
    val contactPhone: String,
    val email: String,
    val address: String
)