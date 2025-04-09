package mx.unam.fciencias.ids.eq1.di

import model.store.sale.repository.DBSalesRepository
import mx.unam.fciencias.ids.eq1.model.store.sales.repository.SalesRepository
import mx.unam.fciencias.ids.eq1.service.store.sales.DBSaleService
import mx.unam.fciencias.ids.eq1.service.store.sales.SaleService
import org.koin.dsl.module

class SalesModule {
    val module = module {
        factory<SalesRepository> { (storeId: Int) ->
            DBSalesRepository(get(), storeId)
        }

        factory<SaleService> { (storeId: Int) ->
            DBSaleService(storeId)
        }
    }
}
