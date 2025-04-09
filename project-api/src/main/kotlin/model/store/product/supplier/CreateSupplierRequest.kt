package mx.unam.fciencias.ids.eq1.model.store.product.supplier

data class CreateSupplierRequest(
    val name: String,
    val contactName: String,
    val contactPhone: String,
    val email: String,
    val address: String
)