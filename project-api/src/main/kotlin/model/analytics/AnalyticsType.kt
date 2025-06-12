package mx.unam.fciencias.ids.eq1.model.analitycs

import kotlinx.serialization.Serializable

/**
 * Tipos de análisis que soporta el sistema
 */
@Serializable
enum class AnalyticsType {
    BEST_SELLERS,    // Productos más vendidos
    WORST_SELLERS,   // Productos menos vendidos
    PROFIT_ANALYSIS, // Análisis de ganancias
    LOSS_ANALYSIS,   // Análisis de pérdidas
    SALES_TREND,     // Tendencia de ventas
    INVENTORY_TURNOVER, // Rotación de inventario
    CATEGORY_PERFORMANCE, // Rendimiento por categoría
    CUSTOMER_BEHAVIOR // Comportamiento de clientes
}