package mx.unam.fciencias.ids.eq1.routes.analytics

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mx.unam.fciencias.ids.eq1.model.analitycs.Analytics
import mx.unam.fciencias.ids.eq1.model.analitycs.ReportFormat
import mx.unam.fciencias.ids.eq1.model.analytics.repository.ChartType
import mx.unam.fciencias.ids.eq1.service.AnalyticsService
import mx.unam.fciencias.ids.eq1.service.GraphicsService
import org.koin.ktor.ext.inject
import java.time.LocalDate

/**
 * Rutas para Analytics API
 */
fun Route.analyticsRoutes() {
    val analyticsService by inject<AnalyticsService>()
    val graphicsService by inject<GraphicsService>()

    route("/analytics") {

        // Obtener análisis de ventas completo
        post("/sales") {
            try {
                val config = call.receive<Analytics>()
                val analytics = analyticsService.getSalesAnalytics(config)
                call.respond(HttpStatusCode.OK, analytics)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
            }
        }

        // Obtener productos más vendidos
        post("/products/best-selling") {
            try {
                val config = call.receive<Analytics>()
                val products = analyticsService.getBestSellingProducts(config)
                call.respond(HttpStatusCode.OK, products)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
            }
        }

        // Obtener productos menos vendidos
        post("/products/worst-selling") {
            try {
                val config = call.receive<Analytics>()
                val products = analyticsService.getWorstSellingProducts(config)
                call.respond(HttpStatusCode.OK, products)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
            }
        }

        // Obtener análisis de ganancias
        post("/profit") {
            try {
                val config = call.receive<Analytics>()
                val profitAnalysis = analyticsService.getProfitAnalysis(config)
                call.respond(HttpStatusCode.OK, profitAnalysis)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
            }
        }

        // Obtener análisis de pérdidas
        post("/loss") {
            try {
                val config = call.receive<Analytics>()
                val lossAnalysis = analyticsService.getLossAnalysis(config)
                call.respond(HttpStatusCode.OK, lossAnalysis)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
            }
        }

        // Obtener tendencia de ventas
        post("/trend") {
            try {
                val config = call.receive<Analytics>()
                val trend = analyticsService.getSalesTrend(config)
                call.respond(HttpStatusCode.OK, trend)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
            }
        }

        // Obtener rendimiento por categoría
        post("/categories") {
            try {
                val config = call.receive<Analytics>()
                val categoryPerformance = analyticsService.getCategoryPerformance(config)
                call.respond(HttpStatusCode.OK, categoryPerformance)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
            }
        }

        // Rutas para gráficos
        route("/charts") {

            // Gráfico de productos más vendidos
            post("/best-selling-products") {
                try {
                    val config = call.receive<Analytics>()
                    val chartBytes = graphicsService.generateBestSellingProductsChart(config)

                    call.response.headers.append(
                        HttpHeaders.ContentDisposition,
                        "attachment; filename=\"best-selling-products.png\""
                    )
                    call.respondBytes(chartBytes, ContentType.Image.PNG)
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
                }
            }

            // Gráfico de tendencia de ventas
            post("/sales-trend") {
                try {
                    val config = call.receive<Analytics>()
                    val chartBytes = graphicsService.generateSalesTrendChart(config)

                    call.response.headers.append(
                        HttpHeaders.ContentDisposition,
                        "attachment; filename=\"sales-trend.png\""
                    )
                    call.respondBytes(chartBytes, ContentType.Image.PNG)
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
                }
            }

            // Gráfico de rendimiento por categoría
            post("/category-performance") {
                try {
                    val config = call.receive<Analytics>()
                    val chartBytes = graphicsService.generateCategoryPerformanceChart(config)

                    call.response.headers.append(
                        HttpHeaders.ContentDisposition,
                        "attachment; filename=\"category-performance.png\""
                    )
                    call.respondBytes(chartBytes, ContentType.Image.PNG)
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
                }
            }

            // Gráfico de ganancias vs pérdidas
            post("/profit-loss") {
                try {
                    val config = call.receive<Analytics>()
                    val chartBytes = graphicsService.generateProfitLossChart(config)

                    call.response.headers.append(
                        HttpHeaders.ContentDisposition,
                        "attachment; filename=\"profit-loss.png\""
                    )
                    call.respondBytes(chartBytes, ContentType.Image.PNG)
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
                }
            }

            // Dashboard completo
            post("/dashboard") {
                try {
                    val config = call.receive<Analytics>()
                    val dashboardBytes = graphicsService.generateDashboard(config)

                    call.response.headers.append(
                        HttpHeaders.ContentDisposition,
                        "attachment; filename=\"dashboard.png\""
                    )
                    call.respondBytes(dashboardBytes, ContentType.Image.PNG)
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
                }
            }

            // Gráfico personalizado
            post("/custom") {
                try {
                    val request = call.receive<CustomChartRequest>()
                    val chartBytes = graphicsService.generateCustomChart(
                        data = request.data,
                        chartType = request.chartType,
                        title = request.title,
                        xLabel = request.xLabel,
                        yLabel = request.yLabel
                    )

                    call.response.headers.append(
                        HttpHeaders.ContentDisposition,
                        "attachment; filename=\"custom-chart.png\""
                    )
                    call.respondBytes(chartBytes, ContentType.Image.PNG)
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
                }
            }
        }

        // Exportar reportes
        route("/export") {
            post("/report") {
                try {
                    val request = call.receive<ExportRequest>()
                    val reportBytes = analyticsService.analyticsRepository.exportReport(
                        config = request.config,
                        format = request.format
                    )

                    val contentType = when (request.format) {
                        ReportFormat.JSON -> ContentType.Application.Json
                        ReportFormat.CSV -> ContentType.Text.CSV
                        ReportFormat.PDF -> ContentType.Application.Pdf
                        ReportFormat.EXCEL -> ContentType.parse("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                    }

                    val fileExtension = when (request.format) {
                        ReportFormat.JSON -> "json"
                        ReportFormat.CSV -> "csv"
                        ReportFormat.PDF -> "pdf"
                        ReportFormat.EXCEL -> "xlsx"
                    }

                    call.response.headers.append(
                        HttpHeaders.ContentDisposition,
                        "attachment; filename=\"analytics-report.$fileExtension\""
                    )
                    call.respondBytes(reportBytes, contentType)
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
                }
            }
        }

        // Endpoint para obtener rangos de fechas rápidos
        get("/date-ranges") {
            val ranges = mapOf(
                "today" to mapOf(
                    "startDate" to LocalDate.now(),
                    "endDate" to LocalDate.now()
                ),
                "yesterday" to mapOf(
                    "startDate" to LocalDate.now().minusDays(1),
                    "endDate" to LocalDate.now().minusDays(1)
                ),
                "last7Days" to mapOf(
                    "startDate" to LocalDate.now().minusDays(7),
                    "endDate" to LocalDate.now()
                ),
                "last30Days" to mapOf(
                    "startDate" to LocalDate.now().minusDays(30),
                    "endDate" to LocalDate.now()
                ),
                "thisMonth" to mapOf(
                    "startDate" to LocalDate.now().withDayOfMonth(1),
                    "endDate" to LocalDate.now()
                ),
                "lastMonth" to mapOf(
                    "startDate" to LocalDate.now().minusMonths(1).withDayOfMonth(1),
                    "endDate" to LocalDate.now().minusMonths(1).withDayOfMonth(
                        LocalDate.now().minusMonths(1).lengthOfMonth()
                    )
                )
            )
            call.respond(HttpStatusCode.OK, ranges)
        }
    }
}