package mx.unam.fciencias.ids.eq1.model.store.product

import kotlinx.serialization.Serializable
import mx.unam.fciencias.ids.eq1.utils.BigDecimalSerializer
import java.math.BigDecimal

@Serializable
data class Product(
    val id: Int,
    val name: String,
    val description: String,
    @Serializable(BigDecimalSerializer::class) val price: BigDecimal,
    val barcode: String,
    @Serializable(BigDecimalSerializer::class) val wholesalePrice: BigDecimal,
    @Serializable(BigDecimalSerializer::class) val retailPrice: BigDecimal,
    val createdAt: Long,
    val stock : Int,
    val minAllowStock : Int
)
