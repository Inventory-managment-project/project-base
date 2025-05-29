package mx.unam.fciencias.ids.eq1.routes.stores.reports

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mx.unam.fciencias.ids.eq1.routes.getStoreIdOrBadRequest
import mx.unam.fciencias.ids.eq1.routes.verifyUserIsOwner
import mx.unam.fciencias.ids.eq1.service.store.reports.ReportService
import org.koin.ktor.ext.inject

fun Route.reportRoutes() {
    authenticate("auth-jwt") {
        route("/stores/{storeId}/reports") {
            val reportService: ReportService by inject()

            get("/sales/products") {
                val storeId = getStoreIdOrBadRequest() ?: return@get
                if (!verifyUserIsOwner(storeId)) {
                    call.respond(HttpStatusCode.Unauthorized, "Not authorized")
                    return@get
                }

                val startDate = call.parameters["startDate"]?.toLongOrNull()
                val endDate = call.parameters["endDate"]?.toLongOrNull()

                if (startDate == null || endDate == null) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid date range")
                    return@get
                }

                val report = reportService.getProductSalesReport(storeId, startDate, endDate)
                call.respond(report)
            }

            get("/sales/date-range") {
                val storeId = getStoreIdOrBadRequest() ?: return@get
                if (!verifyUserIsOwner(storeId)) {
                    call.respond(HttpStatusCode.Unauthorized, "Not authorized")
                    return@get
                }

                val startDate = call.parameters["startDate"]?.toLongOrNull()
                val endDate = call.parameters["endDate"]?.toLongOrNull()

                if (startDate == null || endDate == null) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid date range")
                    return@get
                }

                val report = reportService.getDateRangeSalesReport(storeId, startDate, endDate)
                call.respond(report)
            }
        }
    }
}