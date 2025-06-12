package mx.unam.fciencias.ids.eq1.model.store.product.coupon

import kotlinx.serialization.Serializable
import mx.unam.fciencias.ids.eq1.utils.BigDecimalSerializer
import java.math.BigDecimal

@Serializable
data class Coupon(
    val couponCode : String,
    val description : String? = null,
    val category : String? = null,
    val createdAt : Long,
    @Serializable(BigDecimalSerializer::class) val discount : BigDecimal? = null,
    @Serializable(BigDecimalSerializer::class) val discountAmount : BigDecimal? = null,
    val prodId : Int? = null,
    val valid : Boolean = false,
)