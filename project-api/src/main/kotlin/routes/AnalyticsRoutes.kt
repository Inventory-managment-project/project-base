package routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import model.*
import service.RetailAnalyticsService
import mx.unam.fciencias.ids.eq1.db.store.sales.PAYMENTMETHOD

fun Route.retailAnalyticsRoutes(analyticsService: RetailAnalyticsService) {
    route("/analytics") {
        
        // Endpoint para obtener métricas de ventas
        get("/sales") {
            try {
                val startDate = call.request.queryParameters["startDate"]
                val endDate = call.request.queryParameters["endDate"]
                val storeIdParam = call.request.queryParameters["storeId"]
                val paymentMethodParam = call.request.queryParameters["paymentMethod"]
                val productIdParam = call.request.queryParameters["productId"]
                
                val storeId = storeIdParam?.toIntOrNull()
                val paymentMethod = paymentMethodParam?.let { 
                    try {
                        PAYMENTMETHOD.valueOf(it.uppercase())
                    } catch (e: IllegalArgumentException) {
                        null
                    }
                }
                val productId = productIdParam?.toIntOrNull()
                
                val filter = AnalyticsFilter(
                    startDate = startDate,
                    endDate = endDate,
                    storeId = storeId,
                    paymentMethod = paymentMethod,
                    productId = productId
                )
                
                val salesAnalytics = analyticsService.getSalesAnalytics(filter)
                call.respond(HttpStatusCode.OK, salesAnalytics)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Failed to get sales analytics: ${e.message}"))
            }
        }
        
        // Endpoint para obtener métricas de inventario
        get("/inventory") {
            try {
                val startDate = call.request.queryParameters["startDate"]
                val endDate = call.request.queryParameters["endDate"]
                val storeIdParam = call.request.queryParameters["storeId"]
                
                val storeId = storeIdParam?.toIntOrNull()
                
                val filter = AnalyticsFilter(
                    startDate = startDate,
                    endDate = endDate,
                    storeId = storeId
                )
                
                val inventoryAnalytics = analyticsService.getInventoryAnalytics(filter)
                call.respond(HttpStatusCode.OK, inventoryAnalytics)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Failed to get inventory analytics: ${e.message}"))
            }
        }
        
        // Endpoint para datos en tiempo real
        get("/realtime") {
            try {
                val realtimeMetrics = analyticsService.getRealtimeMetrics()
                call.respond(HttpStatusCode.OK, realtimeMetrics)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Failed to get realtime metrics: ${e.message}"))
            }
        }
        
        // Endpoint para obtener productos con stock bajo
        get("/low-stock") {
            try {
                val storeIdParam = call.request.queryParameters["storeId"]
                val storeId = storeIdParam?.toIntOrNull()
                
                val filter = AnalyticsFilter(storeId = storeId)
                val salesAnalytics = analyticsService.getSalesAnalytics(filter)
                
                call.respond(HttpStatusCode.OK, mapOf("lowStockProducts" to salesAnalytics.lowStockProducts))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Failed to get low stock products: ${e.message}"))
            }
        }
        
        // Endpoint para obtener productos más vendidos
        get("/top-products") {
            try {
                val startDate = call.request.queryParameters["startDate"]
                val endDate = call.request.queryParameters["endDate"]
                val storeIdParam = call.request.queryParameters["storeId"]
                val limitParam = call.request.queryParameters["limit"]
                
                val storeId = storeIdParam?.toIntOrNull()
                val limit = limitParam?.toIntOrNull() ?: 10
                
                val filter = AnalyticsFilter(
                    startDate = startDate,
                    endDate = endDate,
                    storeId = storeId
                )
                
                val salesAnalytics = analyticsService.getSalesAnalytics(filter)
                val topProducts = salesAnalytics.topSellingProducts.take(limit)
                
                call.respond(HttpStatusCode.OK, mapOf("topSellingProducts" to topProducts))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Failed to get top products: ${e.message}"))
            }
        }
        
        // Endpoint para obtener ventas por hora
        get("/sales-by-hour") {
            try {
                val startDate = call.request.queryParameters["startDate"]
                val endDate = call.request.queryParameters["endDate"]
                val storeIdParam = call.request.queryParameters["storeId"]
                
                val storeId = storeIdParam?.toIntOrNull()
                
                val filter = AnalyticsFilter(
                    startDate = startDate,
                    endDate = endDate,
                    storeId = storeId
                )
                
                val salesAnalytics = analyticsService.getSalesAnalytics(filter)
                call.respond(HttpStatusCode.OK, mapOf("salesByHour" to salesAnalytics.salesByHour))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Failed to get sales by hour: ${e.message}"))
            }
        }
        
        // Endpoint para obtener ventas por día
        get("/sales-by-day") {
            try {
                val startDate = call.request.queryParameters["startDate"]
                val endDate = call.request.queryParameters["endDate"]
                val storeIdParam = call.request.queryParameters["storeId"]
                
                val storeId = storeIdParam?.toIntOrNull()
                
                val filter = AnalyticsFilter(
                    startDate = startDate,
                    endDate = endDate,
                    storeId = storeId
                )
                
                val salesAnalytics = analyticsService.getSalesAnalytics(filter)
                call.respond(HttpStatusCode.OK, mapOf("salesByDay" to salesAnalytics.salesByDay))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Failed to get sales by day: ${e.message}"))
            }
        }
        
        // Endpoint para obtener crecimiento de ventas
        get("/sales-growth") {
            try {
                val startDate = call.request.queryParameters["startDate"]
                val endDate = call.request.queryParameters["endDate"]
                val storeIdParam = call.request.queryParameters["storeId"]
                
                val storeId = storeIdParam?.toIntOrNull()
                
                val filter = AnalyticsFilter(
                    startDate = startDate,
                    endDate = endDate,
                    storeId = storeId
                )
                
                val salesAnalytics = analyticsService.getSalesAnalytics(filter)
                call.respond(HttpStatusCode.OK, mapOf("salesGrowth" to salesAnalytics.salesGrowth))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Failed to get sales growth: ${e.message}"))
            }
        }
        
        // Endpoint para dashboard completo
        get("/dashboard") {
            try {
                val startDate = call.request.queryParameters["startDate"]
                val endDate = call.request.queryParameters["endDate"]
                val storeIdParam = call.request.queryParameters["storeId"]
                
                val storeId = storeIdParam?.toIntOrNull()
                
                val filter = AnalyticsFilter(
                    startDate = startDate,
                    endDate = endDate,
                    storeId = storeId
                )
                
                val salesAnalytics = analyticsService.getSalesAnalytics(filter)
                val inventoryAnalytics = analyticsService.getInventoryAnalytics(filter)
                val realtimeMetrics = analyticsService.getRealtimeMetrics()
                
                val dashboard = mapOf(
                    "sales" to salesAnalytics,
                    "inventory" to inventoryAnalytics,
                    "realtime" to realtimeMetrics
                )
                
                call.respond(HttpStatusCode.OK, dashboard)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Failed to get dashboard data: ${e.message}"))
            }
        }
    }
} 