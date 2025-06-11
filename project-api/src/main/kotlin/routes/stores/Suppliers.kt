package mx.unam.fciencias.ids.eq1.routes.stores

import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import mx.unam.fciencias.ids.eq1.model.store.product.supplier.CreateSupplierRequest
import mx.unam.fciencias.ids.eq1.model.store.product.supplier.repository.SupplierRepository
import org.koin.ktor.ext.inject
import io.ktor.server.response.*
import mx.unam.fciencias.ids.eq1.routes.getStoreIdOrBadRequest
import org.koin.core.parameter.parametersOf

fun Route.suppliersRoutes() {
    route("/{storeId}/suppliers") {
        get {
            val storeId = call.getStoreIdOrBadRequest() ?: return@get
            val supplierRepository by call.inject<SupplierRepository> { parametersOf(storeId)}
            val suppliers = supplierRepository.getAll()
            call.respond(HttpStatusCode.OK, suppliers)
        }
        post {
            val storeId = call.getStoreIdOrBadRequest() ?: return@post
            val createSupplierRequest = call.receive<CreateSupplierRequest>()
            val supplierRepository by call.inject<SupplierRepository> { parametersOf(storeId)}
            supplierRepository.add(createSupplierRequest)
            call.respond(HttpStatusCode.Created)
        }
        route("/{supplierId}") {
            get {
                val storeId = call.getStoreIdOrBadRequest() ?: return@get
                val supplierRepository by call.inject<SupplierRepository> { parametersOf(storeId)}
            }
            route("/suppliesProduct/{productId}") {

            }
        }
    }
}