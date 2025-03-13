package mx.unam.fciencias.ids.eq1.di

import mx.unam.fciencias.ids.eq1.model.store.product.repository.DBProductRepository
import mx.unam.fciencias.ids.eq1.model.store.product.repository.ProductRepository
import mx.unam.fciencias.ids.eq1.service.store.product.DBProductService
import mx.unam.fciencias.ids.eq1.service.store.product.ProductService
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.util.concurrent.ConcurrentHashMap

class ProductModule {
    val module = module {
        single(named("productRepositoryCache")) { ConcurrentHashMap<Int, DBProductRepository>() }
        single(named("productServiceCache")) { ConcurrentHashMap<Int, DBProductService>() }

        factory<ProductRepository> { ( storeId : Int) ->
            return@factory get<ConcurrentHashMap<Int, DBProductRepository>>(named("productRepositoryCache")).getOrPut(storeId) {
                DBProductRepository(get(), storeId)
            }
        }
        factory<ProductService> { (storeId : Int) ->
            return@factory get<ConcurrentHashMap<Int, DBProductService>>(named("productServiceCache")).getOrPut(storeId) {
                DBProductService(storeId)
            }
        }
    }

}