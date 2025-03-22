package mx.unam.fciencias.ids.eq1.service.store.product

import mx.unam.fciencias.ids.eq1.model.store.product.Product
import mx.unam.fciencias.ids.eq1.model.store.product.repository.ProductRepository
import org.koin.core.annotation.Factory
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf

@Factory
class DBProductService(storeId : Int) : ProductService, KoinComponent {

    private val productRepository: ProductRepository by inject { return@inject parametersOf(storeId) }

    override suspend fun getAllProducts(): List<Product> {
        return productRepository.getAll()
    }

    override suspend fun getProductById(id: Int): Product? {
        return productRepository.getById(id)
    }

    override suspend fun addProduct(product: Product): Int {
        return productRepository.add(product)
    }

    override suspend fun updateProduct(product: Product): Boolean {
        return productRepository.update(product)
    }

    override suspend fun deleteProductById(id: Int): Boolean {
        return productRepository.delete(id)
    }

    override suspend fun deleteProductByBarcode(barcode: String): Boolean {
        val prodId = productRepository.getByBarcode(barcode)?.id
        return if (prodId != null) {
            productRepository.delete(prodId)
        } else false
    }

    override suspend fun getProductByBarcode(string: String): Product? {
        return productRepository.getByBarcode(string)
    }
}