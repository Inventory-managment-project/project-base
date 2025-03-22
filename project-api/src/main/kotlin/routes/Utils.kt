package mx.unam.fciencias.ids.eq1.routes

import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mx.unam.fciencias.ids.eq1.service.users.UserService
import org.koin.ktor.ext.inject


/**
 * Extracts the user's email from the JWT principal or responds with `400 Bad Request`.
 *
 * **Returns:**
 * - The extracted email as a `String` if found.
 * - `null` if the email is missing, along with a `400 Bad Request` response.
 */
suspend fun RoutingCall.getRequestEmailOrRespondBadRequest(): String? {
    val email = this.principal<JWTPrincipal>()?.payload?.getClaim("email")?.asString()
    if (email == null) this.respond(HttpStatusCode.BadRequest)
    return email
}

/**
 * Verifies if the authenticated user is the owner of the given store. Need `storeId` as parameter. in route.
 *
 * **Returns:**
 * - `true` if the user is the store owner.
 * - `false` if the store ID is invalid, the user is unauthorized, or the email is missing.
 */
suspend fun RoutingCall.verifyUserIsOwner(): Boolean {
    val storeId = this.parameters["storeId"]?.toIntOrNull() ?: return false
    val userEmail = this.getRequestEmailOrRespondBadRequest() ?: return false
    val userService by this.application.inject<UserService>()
    return userService.isOwner(userEmail, storeId)
}

/**
 * Extracts the product ID from the request parameters or responds with `204 No Content`.
 *
 * **Returns:**
 * - The extracted product ID as a `String` if found.
 * - `null` if the product ID is missing, along with a `204 No Content` response and message "Product not found".
 */
suspend fun RoutingCall.getStoreIdOrBadRequest(): Int? {
    val storeId = this.parameters["storeId"]?.toIntOrNull()
    if (storeId == null) this.respond(HttpStatusCode.NotFound)
    return storeId
}
