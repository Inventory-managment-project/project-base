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
                val supplierID = call.parameters["supplierId"]
                if (supplierID == null) {
                    call.respond(HttpStatusCode.NotFound)
                } else {
                    try {
                        val supplier = supplierRepository.getById(supplierID.toInt())
                        if (supplier != null) {
                            call.respond(HttpStatusCode.OK, supplier)
                        } else {
                            call.respond(HttpStatusCode.NotFound)
                        }
                    } catch (_ : Exception) {
                        call.respond(HttpStatusCode.NotFound)
                    }
                }
            }
            route("/suppliesProduct/{productId}") {
                get {
                    val storeId = call.getStoreIdOrBadRequest() ?: return@get
                    val supplierRepository by call.inject<SupplierRepository> { parametersOf(storeId)}
                    val supplierID = call.parameters["supplierId"]
                    val productId = call.parameters["productId"]
                   if (supplierID == null || productId == null) {
                       call.respond(HttpStatusCode.NotFound)
                       return@get
                   }
                    try {
                        val product = supplierRepository.suppliesProducts(supplierID.toInt(), productId.toInt())
                        call.respond(HttpStatusCode.OK, product)
                    } catch ( _ : Exception) {
                        call.respond(HttpStatusCode.NotFound)
                    }
                }

                post {
                    val storeId = call.getStoreIdOrBadRequest() ?: return@post
                    val supplierRepository by call.inject<SupplierRepository> { parametersOf(storeId)}
                    val supplierID = call.parameters["supplierId"]
                    val productId = call.parameters["productId"]
                    if (supplierID == null || productId == null) {
                        call.respond(HttpStatusCode.NotFound)
                        return@post
                    }
                    try {
                        val wasAdded = supplierRepository.addProductSupply(supplierID.toInt(), productId.toInt())
                        call.respond(HttpStatusCode.OK, mapOf("added" to wasAdded))
                    }
                    catch (_ : Exception) {
                        call.respond(HttpStatusCode.NotFound)
                    }
                }
            }
        }
    }
}