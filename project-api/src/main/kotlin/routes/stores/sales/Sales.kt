package mx.unam.fciencias.ids.eq1.routes.stores.sales

import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mx.unam.fciencias.ids.eq1.model.store.sales.Sale
import mx.unam.fciencias.ids.eq1.routes.getRequestEmailOrRespondBadRequest
import mx.unam.fciencias.ids.eq1.routes.getStoreIdOrBadRequest
import mx.unam.fciencias.ids.eq1.routes.verifyUserIsOwner
import mx.unam.fciencias.ids.eq1.service.store.StoreService
import mx.unam.fciencias.ids.eq1.service.store.sales.SaleService
import mx.unam.fciencias.ids.eq1.service.users.UserService
import org.koin.core.parameter.parametersOf
import org.koin.ktor.ext.inject

/**
 * Defines routes for managing sales within stores.
 *
 * **Authentication:** Requires JWT authentication with the "auth-jwt" scheme.
 *
 * **Endpoints:**
 *
 * **Create Sale**
 * - URL: `/{storeId}/sales`
 * - Method: `POST`
 * - Request Body: JSON [Sale]. ID will be ignored when adding the sale to the DB and total recalculated.
 * - Response:
 *     - 201 Created: SaleId
 *     - 404 Not Found: If the store or user is unauthorized.
 *
 * **Get All Sales**
 * - URL: `/{storeId}/sales`
 * - Method: `GET`
 * - Response:
 *     - 200 OK: List of [Sale] for the store.
 *     - 404 Not Found: If the store or user is unauthorized.
 *
 * **Get Sale by ID**
 * - URL: `/{storeId}/sales/{salesId}`
 * - Method: `GET`
 * - Response:
 *     - 200 OK: JSON [Sale].
 *     - 404 Not Found: If the sale or store is not found.
 *
 * **Update Sale**
 * - URL: `/{storeId}/sales/{salesId}`
 * - Method: `PUT`
 * - Request Body: Updated `Sale` object.
 * - Response:
 *     - 200 OK: Sale successfully updated.
 *     - 404 Not Found: If the sale or store is not found.
 *
 * **Delete Sale**
 * - URL: `/{storeId}/sales/{salesId}`
 * - Method: `DELETE`
 * - Response:
 *     - 204 No Content: Sale successfully deleted.
 *     - 404 Not Found: If the sale or store is not found.
 *
 * **Get Sales by Date Range**
 * - URL: `/{storeId}/sales/byDate`
 * - Method: `GET`
 * - Query Parameters: `startDate`, `endDate`
 * - Response:
 *     - 200 OK: List of sales in the specified date range.
 *     - 400 Bad Request: If required parameters are missing.
 */
fun Route.sales() {
    authenticate("auth-jwt") {
        route("/{storeId}/sales") {
            post {
                if(!call.verifyUserIsOwner()) return@post call.respond(HttpStatusCode.NotFound)

                val storeId = call.getStoreIdOrBadRequest() ?: return@post
                val userEmail = call.getRequestEmailOrRespondBadRequest() ?: return@post
                val userService by call.inject<UserService>()
                val user = userService.getUserByEmail(userEmail) ?: return@post call.respond(HttpStatusCode.NotFound)
                val storeService by call.inject<StoreService>()

                val store = storeService.getStoreById(storeId)
                    ?: return@post call.respond(HttpStatusCode.NotFound, "Store not found")

                if (store.owner != user.id) {
                    return@post call.respond(HttpStatusCode.NotFound)
                }

                val sale = call.receive<Sale>()
                val saleService by call.inject<SaleService> { parametersOf(storeId) }

                try {
                    val salesId = saleService.addSale(sale)
                    call.respond(HttpStatusCode.Created, salesId)
                } catch (e: Exception) {
                    call.application.environment.log.error("Could not add sale", e)
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Could not add sale: ${e.message}"))
                }
            }
            get {
                if(!call.verifyUserIsOwner()) return@get call.respond(HttpStatusCode.NotFound)

                val storeId = call.getStoreIdOrBadRequest() ?: return@get

                val saleService by call.application.inject<SaleService> { parametersOf(storeId) }
                call.respond(saleService.getAllSales())
            }
            get("/{salesId}") {
                val storeId = call.parameters["storeId"]?.toIntOrNull()
                    ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid store ID")
                val salesId = call.parameters["salesId"]?.toIntOrNull()
                    ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid sales ID")

                val saleService by call.application.inject<SaleService> { parametersOf(storeId) }
                val sale = saleService.getSaleById(salesId)
                    ?: return@get call.respond(HttpStatusCode.NotFound, "Sale not found")

                call.respond(sale)
            }
            put("/{salesId}") {
                val storeId = call.parameters["storeId"]?.toIntOrNull()
                    ?: return@put call.respond(HttpStatusCode.BadRequest, "Invalid store ID")
                call.parameters["salesId"]?.toIntOrNull()
                    ?: return@put call.respond(HttpStatusCode.BadRequest, "Invalid sales ID")

                // Get authenticated user
                val userEmail = call.principal<JWTPrincipal>()?.payload?.getClaim("email")?.asString()
                    ?: return@put call.respond(HttpStatusCode.BadRequest)
                val userService by call.application.inject<UserService>() { parametersOf(storeId) }
                val user = userService.getUserByEmail(userEmail)
                    ?: return@put call.respond(HttpStatusCode.Forbidden)

                // Verify the store exists
                val storeService by call.application.inject<StoreService>()

                val store = storeService.getStoreById(storeId)
                    ?: return@put call.respond(HttpStatusCode.NotFound, "Store not found")

                // Verify user is the owner of the store
                if (store.owner != user.id) {
                    return@put call.respond(HttpStatusCode.Forbidden, "Not authorized to manage this store")
                }

                val updatedSale = call.receive<Sale>()
                val saleService by call.application.inject<SaleService> { parametersOf(storeId) }

                val result = saleService.updateSale(updatedSale)
                if (result) {
                    call.respond(HttpStatusCode.OK)
                } else {
                    call.respond(HttpStatusCode.NotFound, "Sale not found")
                }
            }
            delete("/{salesId}") {
                val storeId = call.parameters["storeId"]?.toIntOrNull()
                    ?: return@delete call.respond(HttpStatusCode.BadRequest, "Invalid store ID")
                val salesId = call.parameters["salesId"]?.toIntOrNull()
                    ?: return@delete call.respond(HttpStatusCode.BadRequest, "Invalid sales ID")

                val userEmail = call.principal<JWTPrincipal>()?.payload?.getClaim("email")?.asString()
                    ?: return@delete call.respond(HttpStatusCode.BadRequest)
                val userService by call.application.inject<UserService>() { parametersOf(storeId) }

                val user = userService.getUserByEmail(userEmail)
                    ?: return@delete call.respond(HttpStatusCode.Forbidden)

                val storeService by call.application.inject<StoreService>()

                val store = storeService.getStoreById(storeId)
                    ?: return@delete call.respond(HttpStatusCode.NotFound, "Store not found")

                if (store.owner != user.id) {
                    return@delete call.respond(HttpStatusCode.Forbidden, "Not authorized to manage this store")
                }

                val saleService by call.application.inject<SaleService> { parametersOf(storeId) }
                val result = saleService.deleteSale(salesId)

                if (result) {
                    call.respond(HttpStatusCode.NoContent)
                } else {
                    call.respond(HttpStatusCode.NotFound, "Sale not found")
                }
            }
            get("/byDate") {
                val storeId = call.parameters["storeId"]?.toIntOrNull()
                    ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid store ID")

                val userEmail = call.principal<JWTPrincipal>()?.payload?.getClaim("email")?.asString()
                    ?: return@get call.respond(HttpStatusCode.BadRequest)
                val userService by call.application.inject<UserService>() { parametersOf(storeId) }
                val user = userService.getUserByEmail(userEmail)
                    ?: return@get call.respond(HttpStatusCode.Forbidden)

                val storeService by call.application.inject<StoreService>()
                val store = storeService.getStoreById(storeId)
                    ?: return@get call.respond(HttpStatusCode.NotFound, "Store not found")

                if (store.owner != user.id) {
                    return@get call.respond(HttpStatusCode.Forbidden, "Not authorized to access this data")
                }

                val startDate = call.request.queryParameters["startDate"]?.toLongOrNull()
                val endDate = call.request.queryParameters["endDate"]?.toLongOrNull()

                if (startDate == null || endDate == null) {
                    return@get call.respond(HttpStatusCode.BadRequest, "Both startDate and endDate are required")
                }

                val salesService by call.application.inject<SaleService> { parametersOf(storeId) }
                val sales = salesService.getSalesByDateRange(startDate, endDate)
                call.respond(sales)

            }
        }
    }
}