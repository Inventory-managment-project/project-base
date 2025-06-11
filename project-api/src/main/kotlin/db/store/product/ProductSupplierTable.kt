package mx.unam.fciencias.ids.eq1.db.store.product

import mx.unam.fciencias.ids.eq1.db.store.product.supplier.SupplierTable
import org.jetbrains.exposed.dao.id.CompositeIdTable

object ProductSupplierTable : CompositeIdTable("product_supplier") {
    val productId = reference("product_id", ProductTable)
    val supplierId = reference("supplier_id", SupplierTable)

    override val primaryKey = PrimaryKey(productId, supplierId)
}