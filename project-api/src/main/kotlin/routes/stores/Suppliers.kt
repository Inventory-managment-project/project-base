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
import io.ktor.server.routing.delete
import mx.unam.fciencias.ids.eq1.routes.getStoreIdOrBadRequest
import org.koin.core.parameter.parametersOf


/**
 * Defines supplier-related endpoints nested under a specific store.
 *
 * **Endpoints:**
 *
 * **Get All Suppliers**
 * - URL: `/{storeId}/suppliers`
 * - Method: `GET`
 * - Response:
 *     - 200 OK: List of all suppliers for the store.
 *     - 400 Bad Request: If storeId is invalid.
 *
 * **Add Supplier**
 * - URL: `/{storeId}/suppliers`
 * - Method: `POST`
 * - Request Body: [CreateSupplierRequest] with supplier details.
 * - Response:
 *     - 201 Created: Supplier successfully added.
 *     - 400 Bad Request: If storeId is invalid or request is malformed.
 *
 * **Get Supplier by ID**
 * - URL: `/{storeId}/suppliers/{supplierId}`
 * - Method: `GET`
 * - Response:
 *     - 200 OK: Supplier data.
 *     - 404 Not Found: If supplier is not found.
 *
 * **Check if Supplier Supplies Product**
 * - URL: `/{storeId}/suppliers/{supplierId}/suppliesProduct/{productId}`
 * - Method: `GET`
 * - Response:
 *     - 200 OK: The product supplied by the supplier.
 *     - 404 Not Found: If product or supplier is not found.
 *
 * **Add Product Supply to Supplier**
 * - URL: `/{storeId}/suppliers/{supplierId}/suppliesProduct/{productId}`
 * - Method: `POST`
 * - Response:
 *     - 200 OK: Confirmation of added supply.
 *     - 404 Not Found: If supplier or product is not found.
 *
 * **Remove Product Supply from Supplier**
 * - URL: `/{storeId}/suppliers/{supplierId}/suppliesProduct/{productId}`
 * - Method: `DELETE`
 * - Response:
 *     - 200 OK: Confirmation of deletion status.
 *     - 404 Not Found: If supplier or product is not found.
 */
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

                delete {
                    val storeId = call.getStoreIdOrBadRequest() ?: return@delete
                    val supplierRepository by call.inject<SupplierRepository> { parametersOf(storeId)}
                    val supplierID = call.parameters["supplierId"]
                    val productId = call.parameters["productId"]
                    if (supplierID == null || productId == null) {
                        call.respond(HttpStatusCode.NotFound)
                        return@delete
                    }
                    try {
                        val wasDeleted = supplierRepository.removeProductSupply(supplierID.toInt(), productId.toInt())
                        call.respond(HttpStatusCode.OK, mapOf("deleted" to wasDeleted))
                    }
                    catch (_ : Exception) {
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