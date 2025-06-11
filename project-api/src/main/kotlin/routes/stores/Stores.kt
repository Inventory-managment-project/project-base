package mx.unam.fciencias.ids.eq1.routes.stores

import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import mx.unam.fciencias.ids.eq1.model.store.CreateStoreRequest
import mx.unam.fciencias.ids.eq1.service.users.UserService
import io.ktor.server.response.*
import mx.unam.fciencias.ids.eq1.routes.getRequestEmailOrRespondBadRequest
import mx.unam.fciencias.ids.eq1.routes.getStoreIdOrBadRequest
import mx.unam.fciencias.ids.eq1.routes.stores.sales.salesRoutes
import mx.unam.fciencias.ids.eq1.service.store.StoreService
import org.koin.ktor.ext.inject

/**
 * Defines routes for managing stores and their associated data. [productsRoutes] and [salesRoutes] routes live inside here in `/store`.
 *
 * **Authentication:** Requires JWT authentication with the "auth-jwt" scheme.
 *
 * **Endpoints:**
 *
 * **Create Store**
 * - URL: `/createStore`
 * - Method: `POST`
 * - Request Body: [CreateStoreRequest] object containing store details.
 * - Response:
 *     - 201 Created: Store successfully created.
 *     - 403 Forbidden: If the user is unauthorized.
 *
 * **Get All Stores by Owner**
 * - URL: `/stores`
 * - Method: `GET`
 * - Response:
 *     - 200 OK: List of [Store] owned by the authenticated user.
 *     - 404 Not Found: If no stores are found or the user is unauthorized.
 *
 * **Get Store Details by ID**
 * - URL: `/stores/{storeId}`
 * - Method: `GET`
 * - Response:
 *     - 200 OK: [Store] details.
 *     - 404 Not Found: If the store is not found.
 *
 * **Get Stores for Owner**
 * - URL: `/stores/owner`
 * - Method: `GET`
 * - Response:
 *     - 200 OK: List of [Store] owned by the authenticated user.
 *     - 404 Not Found: If no stores are found or the user is unauthorized.
 */
fun Route.storeRoutes() {
    authenticate("auth-jwt") {
        route("createStore") {
            post {
                val createStoreRequest = call.receive<CreateStoreRequest>()
                val userEmail = call.getRequestEmailOrRespondBadRequest() ?: return@post
                val userService by call.inject<UserService>()
                val user = userService.getUserByEmail(userEmail)
                    ?: return@post call.respond(HttpStatusCode.Forbidden)
                val storeService by call.inject<StoreService>()
                storeService.createStore(createStoreRequest, user)
                call.respond(HttpStatusCode.Created)
            }
        }
        route("/stores") {
            productsRoutes()
            salesRoutes()
            suppliersRoutes()
            route("/{storeId}") {
                get {
                    val storeId = call.getStoreIdOrBadRequest() ?: return@get
                    val storeService by call.inject<StoreService>()
                    val store = storeService.getStoreById(storeId) ?: return@get call.respond(HttpStatusCode.NotFound)
                    call.respond(HttpStatusCode.OK, store)
                }
            }
            get {
                val email = call.getRequestEmailOrRespondBadRequest() ?: return@get
                val userService by call.inject<UserService>()
                val ownerId = userService.getUserByEmail(email)?.id ?: return@get call.respond(HttpStatusCode.NotFound)
                if (userService.getUserByEmail(email)?.id != ownerId) return@get call.respond(HttpStatusCode.Forbidden)
                val storeService by call.inject<StoreService>()
                val stores = storeService.getStoresByOwner(ownerId)
                call.respond(HttpStatusCode.OK, stores)
            }
            get("/owner") {
                val email = call.getRequestEmailOrRespondBadRequest() ?: return@get
                val userService by call.inject<UserService>()
                val ownerId = userService.getUserByEmail(email)?.id ?: return@get
                if (userService.getUserByEmail(email)?.id != ownerId) return@get call.respond(HttpStatusCode.Forbidden)
                val storeService by call.inject<StoreService>()
                val stores = storeService.getStoresByOwner(ownerId)
                call.respond(HttpStatusCode.OK, stores)
            }
        }
    }
}