package mx.unam.fciencias.ids.eq1.routes.users

import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mx.unam.fciencias.ids.eq1.model.user.PublicUser
import mx.unam.fciencias.ids.eq1.routes.getRequestEmailOrRespondBadRequest
import mx.unam.fciencias.ids.eq1.service.users.UserService
import org.koin.ktor.ext.inject



/**
 * Endpoint to retrieve user details based on their email.
 *
 * **Authentication:** Requires JWT authentication with the "auth-jwt" scheme.
 * Authentication can be provided via:
 * - `Authorization` header (e.g., `Bearer <token>`)
 * - Cookie named `token`
 *
 * **Request Format:**
 * - HTTP Method: GET
 * - Endpoint: `/user`
 * - Headers (optional if cookie is used):
 *   - `Authorization: Bearer <token>`
 * - Body: JSON object containing an `email` field.
 *
 * **Response Format:**
 * - Success: Returns the user details in JSON format.
 * - Failure:
 *   - 400 Bad Request: If the email is missing or malformed.
 *   - 404 Not Found: If no user is found with the provided email.
 *
 * Response (Success): JSON of [mx.unam.fciencias.ids.eq1.model.user.PublicUser]
 *
 * Example Response (Not Found):
 * ```json
 * "Not Found"
 * ```
 */
fun Route.usersRoutes() {
    authenticate("auth-jwt") {
        route("/user") {
            get {
                val email = call.getRequestEmailOrRespondBadRequest() ?: return@get
                val userService by call.inject<UserService>()
                val user = userService.getUserByEmail(email)
                if (user != null) {
                    val responseUser = PublicUser(user.name, user.email, user.createdAt)
                    call.respond(responseUser)
                } else {
                    call.respond(HttpStatusCode.NoContent)
                }
            }
        }
    }
}