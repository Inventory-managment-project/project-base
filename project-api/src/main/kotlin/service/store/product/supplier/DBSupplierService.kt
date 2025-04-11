package mx.unam.fciencias.ids.eq1.service.store.product.supplier

import mx.unam.fciencias.ids.eq1.model.store.product.supplier.Supplier
import mx.unam.fciencias.ids.eq1.model.store.product.supplier.repository.SupplierRepository
import mx.unam.fciencias.ids.eq1.model.store.product.supplier.CreateSupplierRequest
import mx.unam.fciencias.ids.eq1.model.store.product.supplier.UpdateSupplierRequest
import org.koin.core.annotation.Factory
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf

@Factory
class DBSupplierService(storeId: Int) : SupplierService, KoinComponent {

    private val supplierRepository: SupplierRepository by inject { return@inject parametersOf(storeId) }

    override suspend fun getAllSuppliers(): List<Supplier>{
        return supplierRepository.getAll()
    }

    override suspend fun getSupplierById(id: Int): Supplier? {
        return supplierRepository.getById(id)
    }

    override suspend fun createSupplier(request: CreateSupplierRequest): Int {
        TODO("Not yet implemented")
    }

    override suspend fun updateSupplier(request: UpdateSupplierRequest): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun deleteSupplier(id: Int): Boolean {
        return supplierRepository.delete(id)
    }
}
