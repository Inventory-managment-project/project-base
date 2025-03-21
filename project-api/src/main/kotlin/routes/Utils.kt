package mx.unam.fciencias.ids.eq1.routes

import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


suspend fun RoutingCall.getRequestEmailOrRespondBadRequest(): String? {
    val email = this.principal<JWTPrincipal>()?.payload?.getClaim("email")?.asString()
    if (email == null) this.respond(HttpStatusCode.BadRequest)
    return email
}
