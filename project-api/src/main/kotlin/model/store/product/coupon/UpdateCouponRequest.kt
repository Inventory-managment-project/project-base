package mx.unam.fciencias.ids.eq1.model.store.product.coupon

import kotlinx.serialization.Serializable
import java.math.BigDecimal
import mx.unam.fciencias.ids.eq1.utils.BigDecimalSerializer


@Serializable
data class UpdateCouponRequest(
    val description: String? = null,
    val category: String? = null,
    @Serializable(BigDecimalSerializer::class) val discount: BigDecimal? = null,
    @Serializable(BigDecimalSerializer::class) val discountAmount: BigDecimal? = null,
    val prodId: Int? = null,
    val validFrom: Long? = null,
    val validUntil: Long? = null
)
