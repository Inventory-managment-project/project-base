package mx.unam.fciencias.ids.eq1.db.store.product.supplier

import mx.unam.fciencias.ids.eq1.db.store.StoreTable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.CurrentTimestamp
import org.jetbrains.exposed.sql.javatime.timestamp

object SupplierTable : IntIdTable("providers"){
    val storeId = reference("id_store", StoreTable)
    val supplierId = integer("id_provider")
    val name = varchar("name", 255)
    val contactName = varchar("contact_name", 255)
    val contactPhone = varchar("contact_phone", 20)
    val email = varchar("email", 255)
    val address = varchar("address", length = 255)
    val createdAt = timestamp("created_at").defaultExpression(CurrentTimestamp)

    init {
        uniqueIndex("unique_store_provider", storeId, supplierId)
        index(isUnique = false, storeId)
    }
}