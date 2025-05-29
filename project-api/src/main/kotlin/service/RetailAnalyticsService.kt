package service

import model.*
import mx.unam.fciencias.ids.eq1.db.store.sales.PAYMENTMETHOD
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class RetailAnalyticsService {
    
    // Datos de demostración para el sistema de punto de venta
    // En una implementación real, estos datos vendrían de la base de datos
    
    fun getSalesAnalytics(filter: AnalyticsFilter): SalesAnalytics {
        return SalesAnalytics(
            totalSales = 1247L,
            totalRevenue = BigDecimal("45678.50"),
            averageTicket = BigDecimal("36.65"),
            salesByPaymentMethod = mapOf(
                "CASH" to 756L,
                "CARD" to 491L
            ),
            revenueByPaymentMethod = mapOf(
                "CASH" to BigDecimal("27890.30"),
                "CARD" to BigDecimal("17788.20")
            ),
            topSellingProducts = getTopSellingProducts(),
            salesByHour = getSalesByHour(),
            salesByDay = getSalesByDay(),
            lowStockProducts = getLowStockProducts(),
            salesGrowth = getSalesGrowth()
        )
    }
    
    fun getInventoryAnalytics(filter: AnalyticsFilter): InventoryAnalytics {
        return InventoryAnalytics(
            totalProducts = 234L,
            totalStockValue = BigDecimal("89456.75"),
            lowStockCount = 12L,
            outOfStockCount = 3L,
            averageStockLevel = 67.8,
            topValueProducts = getTopValueProducts(),
            stockMovement = getStockMovement()
        )
    }
    
    fun getRealtimeMetrics(): RealtimeMetrics {
        return RealtimeMetrics(
            todaySales = 89L,
            todayRevenue = BigDecimal("3245.80"),
            currentHourSales = 7L,
            averageTicketToday = BigDecimal("36.47"),
            topSellingProductToday = ProductSalesMetric(
                productId = 101,
                productName = "Coca Cola 600ml",
                quantitySold = BigDecimal("24"),
                revenue = BigDecimal("432.00"),
                salesCount = 24L,
                averagePrice = BigDecimal("18.00")
            ),
            recentSales = getRecentSales()
        )
    }
    
    private fun getTopSellingProducts(): List<ProductSalesMetric> {
        return listOf(
            ProductSalesMetric(
                productId = 101,
                productName = "Coca Cola 600ml",
                quantitySold = BigDecimal("456"),
                revenue = BigDecimal("8208.00"),
                salesCount = 456L,
                averagePrice = BigDecimal("18.00")
            ),
            ProductSalesMetric(
                productId = 102,
                productName = "Pan Bimbo Integral",
                quantitySold = BigDecimal("234"),
                revenue = BigDecimal("7020.00"),
                salesCount = 234L,
                averagePrice = BigDecimal("30.00")
            ),
            ProductSalesMetric(
                productId = 103,
                productName = "Leche Lala 1L",
                quantitySold = BigDecimal("189"),
                revenue = BigDecimal("4914.00"),
                salesCount = 189L,
                averagePrice = BigDecimal("26.00")
            ),
            ProductSalesMetric(
                productId = 104,
                productName = "Sabritas Clásicas",
                quantitySold = BigDecimal("167"),
                revenue = BigDecimal("2505.00"),
                salesCount = 167L,
                averagePrice = BigDecimal("15.00")
            ),
            ProductSalesMetric(
                productId = 105,
                productName = "Agua Bonafont 1.5L",
                quantitySold = BigDecimal("145"),
                revenue = BigDecimal("2175.00"),
                salesCount = 145L,
                averagePrice = BigDecimal("15.00")
            )
        )
    }
    
    private fun getSalesByHour(): List<HourlySalesMetric> {
        return listOf(
            HourlySalesMetric(8, 12L, BigDecimal("456.50")),
            HourlySalesMetric(9, 23L, BigDecimal("892.30")),
            HourlySalesMetric(10, 34L, BigDecimal("1245.80")),
            HourlySalesMetric(11, 45L, BigDecimal("1678.90")),
            HourlySalesMetric(12, 67L, BigDecimal("2456.70")),
            HourlySalesMetric(13, 78L, BigDecimal("2890.40")),
            HourlySalesMetric(14, 56L, BigDecimal("2134.60")),
            HourlySalesMetric(15, 43L, BigDecimal("1567.80")),
            HourlySalesMetric(16, 38L, BigDecimal("1389.20")),
            HourlySalesMetric(17, 52L, BigDecimal("1923.40")),
            HourlySalesMetric(18, 61L, BigDecimal("2245.70")),
            HourlySalesMetric(19, 48L, BigDecimal("1756.30")),
            HourlySalesMetric(20, 35L, BigDecimal("1289.50")),
            HourlySalesMetric(21, 22L, BigDecimal("823.40"))
        )
    }
    
    private fun getSalesByDay(): List<DailySalesMetric> {
        return listOf(
            DailySalesMetric(
                date = "2024-01-15",
                salesCount = 156L,
                revenue = BigDecimal("5678.90"),
                averageTicket = BigDecimal("36.40"),
                transactionsByPaymentMethod = mapOf("CASH" to 89L, "CARD" to 67L)
            ),
            DailySalesMetric(
                date = "2024-01-16",
                salesCount = 178L,
                revenue = BigDecimal("6234.50"),
                averageTicket = BigDecimal("35.02"),
                transactionsByPaymentMethod = mapOf("CASH" to 102L, "CARD" to 76L)
            ),
            DailySalesMetric(
                date = "2024-01-17",
                salesCount = 134L,
                revenue = BigDecimal("4892.30"),
                averageTicket = BigDecimal("36.51"),
                transactionsByPaymentMethod = mapOf("CASH" to 78L, "CARD" to 56L)
            ),
            DailySalesMetric(
                date = "2024-01-18",
                salesCount = 189L,
                revenue = BigDecimal("7123.80"),
                averageTicket = BigDecimal("37.69"),
                transactionsByPaymentMethod = mapOf("CASH" to 112L, "CARD" to 77L)
            ),
            DailySalesMetric(
                date = "2024-01-19",
                salesCount = 167L,
                revenue = BigDecimal("6045.70"),
                averageTicket = BigDecimal("36.20"),
                transactionsByPaymentMethod = mapOf("CASH" to 95L, "CARD" to 72L)
            ),
            DailySalesMetric(
                date = "2024-01-20",
                salesCount = 201L,
                revenue = BigDecimal("7456.90"),
                averageTicket = BigDecimal("37.10"),
                transactionsByPaymentMethod = mapOf("CASH" to 118L, "CARD" to 83L)
            ),
            DailySalesMetric(
                date = "2024-01-21",
                salesCount = 222L,
                revenue = BigDecimal("8234.60"),
                averageTicket = BigDecimal("37.09"),
                transactionsByPaymentMethod = mapOf("CASH" to 134L, "CARD" to 88L)
            )
        )
    }
    
    private fun getLowStockProducts(): List<LowStockProduct> {
        return listOf(
            LowStockProduct(
                productId = 201,
                productName = "Aceite Capullo 1L",
                currentStock = BigDecimal("2"),
                minAllowStock = 10,
                stockLevel = StockLevel.CRITICAL,
                lastSaleDate = "2024-01-21"
            ),
            LowStockProduct(
                productId = 202,
                productName = "Azúcar Estándar 1kg",
                currentStock = BigDecimal("5"),
                minAllowStock = 15,
                stockLevel = StockLevel.LOW,
                lastSaleDate = "2024-01-21"
            ),
            LowStockProduct(
                productId = 203,
                productName = "Papel Higiénico Suave",
                currentStock = BigDecimal("8"),
                minAllowStock = 20,
                stockLevel = StockLevel.LOW,
                lastSaleDate = "2024-01-20"
            ),
            LowStockProduct(
                productId = 204,
                productName = "Detergente Ariel 1kg",
                currentStock = BigDecimal("12"),
                minAllowStock = 25,
                stockLevel = StockLevel.WARNING,
                lastSaleDate = "2024-01-19"
            )
        )
    }
    
    private fun getSalesGrowth(): List<SalesGrowthMetric> {
        return listOf(
            SalesGrowthMetric(
                date = "2024-01-15",
                salesCount = 156L,
                revenue = BigDecimal("5678.90"),
                growthPercentage = 5.2,
                revenueGrowthPercentage = 7.8
            ),
            SalesGrowthMetric(
                date = "2024-01-16",
                salesCount = 178L,
                revenue = BigDecimal("6234.50"),
                growthPercentage = 14.1,
                revenueGrowthPercentage = 9.8
            ),
            SalesGrowthMetric(
                date = "2024-01-17",
                salesCount = 134L,
                revenue = BigDecimal("4892.30"),
                growthPercentage = -24.7,
                revenueGrowthPercentage = -21.5
            ),
            SalesGrowthMetric(
                date = "2024-01-18",
                salesCount = 189L,
                revenue = BigDecimal("7123.80"),
                growthPercentage = 41.0,
                revenueGrowthPercentage = 45.6
            ),
            SalesGrowthMetric(
                date = "2024-01-19",
                salesCount = 167L,
                revenue = BigDecimal("6045.70"),
                growthPercentage = -11.6,
                revenueGrowthPercentage = -15.1
            ),
            SalesGrowthMetric(
                date = "2024-01-20",
                salesCount = 201L,
                revenue = BigDecimal("7456.90"),
                growthPercentage = 20.4,
                revenueGrowthPercentage = 23.3
            ),
            SalesGrowthMetric(
                date = "2024-01-21",
                salesCount = 222L,
                revenue = BigDecimal("8234.60"),
                growthPercentage = 10.4,
                revenueGrowthPercentage = 10.4
            )
        )
    }
    
    private fun getTopValueProducts(): List<ProductValueMetric> {
        return listOf(
            ProductValueMetric(
                productId = 301,
                productName = "iPhone 15 Pro",
                stock = BigDecimal("5"),
                retailPrice = BigDecimal("25999.00"),
                totalValue = BigDecimal("129995.00")
            ),
            ProductValueMetric(
                productId = 302,
                productName = "Samsung Galaxy S24",
                stock = BigDecimal("8"),
                retailPrice = BigDecimal("18999.00"),
                totalValue = BigDecimal("151992.00")
            ),
            ProductValueMetric(
                productId = 303,
                productName = "MacBook Air M2",
                stock = BigDecimal("3"),
                retailPrice = BigDecimal("32999.00"),
                totalValue = BigDecimal("98997.00")
            )
        )
    }
    
    private fun getStockMovement(): List<StockMovementMetric> {
        return listOf(
            StockMovementMetric(
                productId = 101,
                productName = "Coca Cola 600ml",
                initialStock = BigDecimal("500"),
                currentStock = BigDecimal("44"),
                totalSold = BigDecimal("456"),
                turnoverRate = 91.2
            ),
            StockMovementMetric(
                productId = 102,
                productName = "Pan Bimbo Integral",
                initialStock = BigDecimal("300"),
                currentStock = BigDecimal("66"),
                totalSold = BigDecimal("234"),
                turnoverRate = 78.0
            ),
            StockMovementMetric(
                productId = 103,
                productName = "Leche Lala 1L",
                initialStock = BigDecimal("250"),
                currentStock = BigDecimal("61"),
                totalSold = BigDecimal("189"),
                turnoverRate = 75.6
            )
        )
    }
    
    private fun getRecentSales(): List<RecentSaleMetric> {
        return listOf(
            RecentSaleMetric(
                saleId = 1001,
                total = BigDecimal("156.50"),
                paymentMethod = PAYMENTMETHOD.CARD,
                timestamp = LocalDateTime.now().minusMinutes(5).toString(),
                productCount = 4
            ),
            RecentSaleMetric(
                saleId = 1002,
                total = BigDecimal("89.30"),
                paymentMethod = PAYMENTMETHOD.CASH,
                timestamp = LocalDateTime.now().minusMinutes(12).toString(),
                productCount = 3
            ),
            RecentSaleMetric(
                saleId = 1003,
                total = BigDecimal("234.80"),
                paymentMethod = PAYMENTMETHOD.CARD,
                timestamp = LocalDateTime.now().minusMinutes(18).toString(),
                productCount = 6
            ),
            RecentSaleMetric(
                saleId = 1004,
                total = BigDecimal("67.20"),
                paymentMethod = PAYMENTMETHOD.CASH,
                timestamp = LocalDateTime.now().minusMinutes(25).toString(),
                productCount = 2
            ),
            RecentSaleMetric(
                saleId = 1005,
                total = BigDecimal("345.60"),
                paymentMethod = PAYMENTMETHOD.CARD,
                timestamp = LocalDateTime.now().minusMinutes(31).toString(),
                productCount = 8
            )
        )
    }
} 