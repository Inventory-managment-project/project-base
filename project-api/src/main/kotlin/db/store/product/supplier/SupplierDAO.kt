package mx.unam.fciencias.ids.eq1.db.store.product.supplier

import mx.unam.fciencias.ids.eq1.db.user.UserTable
import mx.unam.fciencias.ids.eq1.model.store.product.supplier.Supplier
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class SupplierDAO (id: EntityID<Int>) : IntEntity(id){
    companion object : IntEntityClass<SupplierDAO>(SupplierTable) {
        fun supplierDaoToModel(dao: SupplierDAO) = Supplier(
            dao.supplierId,
            dao.name,
            dao.contactName,
            dao.contactPhone,
            dao.email,
            dao.address,
            dao.createdAt.epochSecond
        )
    }

    var supplierId by SupplierTable.supplierId
    var name by SupplierTable.name
    var email by SupplierTable.email
    var address by SupplierTable.address
    var contactName by SupplierTable.contactName
    var contactPhone by SupplierTable.contactPhone
    var createdAt by SupplierTable.createdAt
    var storeId by SupplierTable.storeId
}