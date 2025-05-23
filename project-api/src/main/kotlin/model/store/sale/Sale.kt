package mx.unam.fciencias.ids.eq1.model.store.sales

import kotlinx.serialization.Serializable
import mx.unam.fciencias.ids.eq1.db.store.sales.PAYMENTMETHOD
import mx.unam.fciencias.ids.eq1.utils.BigDecimalSerializer
import java.math.BigDecimal

@Serializable
data class Sale (
    val id : Int,
    @Serializable(BigDecimalSerializer ::class) val total : BigDecimal,
    val paymentmethod : PAYMENTMETHOD,
    val created : Long,
    val products : List<Pair<Int, @Serializable(BigDecimalSerializer ::class) BigDecimal>>,
    @Serializable(BigDecimalSerializer ::class) val subtotal  : BigDecimal
)