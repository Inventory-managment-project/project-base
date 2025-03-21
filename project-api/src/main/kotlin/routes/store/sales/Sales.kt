package mx.unam.fciencias.ids.eq1.routes.store.sales

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mx.unam.fciencias.ids.eq1.model.store.sales.Sales
import mx.unam.fciencias.ids.eq1.service.store.StoreService
import mx.unam.fciencias.ids.eq1.service.store.sales.SaleService
import mx.unam.fciencias.ids.eq1.service.users.UserService
import org.koin.core.parameter.parametersOf
import org.koin.ktor.ext.inject

fun Route.sales() {
    val storeService by inject<StoreService>()
    val userService by inject<UserService>()

    // All operations below require authentication
    authenticate("auth-jwt") {
        // Create a new sale
        post {
            val storeId = call.parameters["storeId"]?.toIntOrNull()
                ?: return@post call.respond(HttpStatusCode.NotFound, "Invalid store ID")

            // Get authenticated user
            val userEmail = call.principal<JWTPrincipal>()?.payload?.getClaim("email")?.asString()
                ?: return@post call.respond(HttpStatusCode.NotFound)
            val user = userService.getUserByEmail(userEmail)
                ?: return@post call.respond(HttpStatusCode.NotFound)

            val userServiceInstance by call.application.inject<UserService>()
            if (!userServiceInstance.isOwner(userEmail, storeId)) {
                return@post call.respond(HttpStatusCode.NotFound)
            }
            // Verify the store exists
            val store = storeService.getStoreById(storeId)
                ?: return@post call.respond(HttpStatusCode.NotFound, "Store not found")

            // Verify user is the owner of the store
            if (store.owner != user.id) {
                return@post call.respond(HttpStatusCode.NotFound, "Not authorized to manage this store")
            }

            val sale = call.receive<Sales>()
            val saleService by call.application.inject<SaleService> { parametersOf(storeId) }

            try {
                val salesId = saleService.addSale(sale)
                call.respond(HttpStatusCode.Created, mapOf("id" to salesId))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to (e.message ?: "Unknown error")))
            }
        }
        route("/stores/{storeId}/sales") {
            // Get all sales for a store - Public endpoint, no authentication required
            get {
                val storeId = call.parameters["storeId"]?.toIntOrNull()
                    ?: return@get call.respond(HttpStatusCode.NotFound, "Invalid store ID")

                val saleService by call.application.inject<SaleService> { parametersOf(storeId) }
                call.respond(saleService.getAllSales())
            }

            // Get a specific sale by ID - Public endpoint, no authentication required
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

            // Update a sale
            put("/{salesId}") {
                val storeId = call.parameters["storeId"]?.toIntOrNull()
                    ?: return@put call.respond(HttpStatusCode.BadRequest, "Invalid store ID")
                val salesId = call.parameters["salesId"]?.toIntOrNull()
                    ?: return@put call.respond(HttpStatusCode.BadRequest, "Invalid sales ID")

                // Get authenticated user
                val userEmail = call.principal<JWTPrincipal>()?.payload?.getClaim("email")?.asString()
                    ?: return@put call.respond(HttpStatusCode.BadRequest)
                val user = userService.getUserByEmail(userEmail)
                    ?: return@put call.respond(HttpStatusCode.Forbidden)

                // Verify the store exists
                val store = storeService.getStoreById(storeId)
                    ?: return@put call.respond(HttpStatusCode.NotFound, "Store not found")

                // Verify user is the owner of the store
                if (store.owner != user.id) {
                    return@put call.respond(HttpStatusCode.Forbidden, "Not authorized to manage this store")
                }

                val updatedSale = call.receive<Sales>()
                val saleService by call.application.inject<SaleService> { parametersOf(storeId) }

                val result = saleService.updateSale(updatedSale)
                if (result) {
                    call.respond(HttpStatusCode.OK)
                } else {
                    call.respond(HttpStatusCode.NotFound, "Sale not found")
                }
            }

            // Delete a sale
            delete("/{salesId}") {
                val storeId = call.parameters["storeId"]?.toIntOrNull()
                    ?: return@delete call.respond(HttpStatusCode.BadRequest, "Invalid store ID")
                val salesId = call.parameters["salesId"]?.toIntOrNull()
                    ?: return@delete call.respond(HttpStatusCode.BadRequest, "Invalid sales ID")

                // Get authenticated user
                val userEmail = call.principal<JWTPrincipal>()?.payload?.getClaim("email")?.asString()
                    ?: return@delete call.respond(HttpStatusCode.BadRequest)
                val user = userService.getUserByEmail(userEmail)
                    ?: return@delete call.respond(HttpStatusCode.Forbidden)

                // Verify the store exists
                val store = storeService.getStoreById(storeId)
                    ?: return@delete call.respond(HttpStatusCode.NotFound, "Store not found")

                // Verify user is the owner of the store
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

            // Get sales by date range - Authenticated and owner only
            get("/byDate") {
                val storeId = call.parameters["storeId"]?.toIntOrNull()
                    ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid store ID")

                // Get authenticated user
                val userEmail = call.principal<JWTPrincipal>()?.payload?.getClaim("email")?.asString()
                    ?: return@get call.respond(HttpStatusCode.BadRequest)
                val user = userService.getUserByEmail(userEmail)
                    ?: return@get call.respond(HttpStatusCode.Forbidden)

                // Verify the store exists
                val store = storeService.getStoreById(storeId)
                    ?: return@get call.respond(HttpStatusCode.NotFound, "Store not found")

                // Verify user is the owner of the store
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

            // Get sales summary/reports - Authenticated and owner only
            get("/reports") {
                val storeId = call.parameters["storeId"]?.toIntOrNull()
                    ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid store ID")

                // Get authenticated user
                val userEmail = call.principal<JWTPrincipal>()?.payload?.getClaim("email")?.asString()
                    ?: return@get call.respond(HttpStatusCode.BadRequest)
                val user = userService.getUserByEmail(userEmail)
                    ?: return@get call.respond(HttpStatusCode.Forbidden)

                // Verify the store exists
                val store = storeService.getStoreById(storeId)
                    ?: return@get call.respond(HttpStatusCode.NotFound, "Store not found")

                // Verify user is the owner of the store
                if (store.owner != user.id) {
                    return@get call.respond(HttpStatusCode.Forbidden, "Not authorized to access reports")
                }

                val period = call.request.queryParameters["period"] ?: "all" // daily, weekly, monthly, all

                val saleService by call.application.inject<SaleService> { parametersOf(storeId) }
                //val reports = saleService.generateSalesReports(period)
                call.respond(HttpStatusCode.NotImplemented)
            }
        }
    }
}