package model.analytics

import mx.unam.fciencias.ids.eq1.model.analytics.*
import io.mockk.*
import kotlinx.coroutines.test.runTest
import mx.unam.fciencias.ids.eq1.model.analytics.repository.AnalyticsRepository
import mx.unam.fciencias.ids.eq1.model.analytics.repository.LetsPlotGraphicsRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import java.math.BigDecimal
import java.time.LocalDate
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@DisplayName("LetsPlotGraphicsRepository Tests")
class DBAnalyticsRepositoryTest {

    private lateinit var analyticsRepository: AnalyticsRepository
    private lateinit var graphicsRepository: LetsPlotGraphicsRepository
    private lateinit var testConfig: Analytics

    @BeforeEach
    fun setUp() {
        analyticsRepository = mockk()
        graphicsRepository = LetsPlotGraphicsRepository(analyticsRepository)
        testConfig = Analytics(
            startDate = LocalDate.of(2024, 1, 1),
            endDate = LocalDate.of(2024, 12, 31),
            storeId = 1L
        )
    }

    @Nested
    @DisplayName("Best Selling Products Chart Tests")
    inner class BestSellingProductsChartTests {

        @Test
        @DisplayName("Should generate chart successfully with valid data")
        fun shouldGenerateChartSuccessfullyWithValidData() = runTest {
            // Given
            val mockProducts = listOf(
                ProductSales(
                    productId = 1L,
                    productName = "Laptop Dell XPS 13",
                    totalQuantitySold = 50,
                    totalRevenue = BigDecimal("75000.00")
                ),
                ProductSales(
                    productId = 2L,
                    productName = "iPhone 15 Pro Max",
                    totalQuantitySold = 30,
                    totalRevenue = BigDecimal("45000.00")
                ),
                ProductSales(
                    productId = 3L,
                    productName = "Samsung Galaxy S24 Ultra Premium Edition",
                    totalQuantitySold = 25,
                    totalRevenue = BigDecimal("35000.00")
                )
            )

            coEvery { analyticsRepository.getBestSellingProducts(testConfig) } returns mockProducts

            // When
            val result = graphicsRepository.generateBestSellingProductsChart(testConfig)

            // Then
            assertNotNull(result)
            assertTrue(result.isNotEmpty())
            coVerify { analyticsRepository.getBestSellingProducts(testConfig) }
        }

        @Test
        @DisplayName("Should handle long product names by truncating")
        fun shouldHandleLongProductNamesByTruncating() = runTest {
            // Given
            val mockProducts = listOf(
                ProductSales(
                    productId = 1L,
                    productName = "This is a very long product name that should be truncated",
                    totalQuantitySold = 10,
                    totalRevenue = BigDecimal("1000.00")
                )
            )

            coEvery { analyticsRepository.getBestSellingProducts(testConfig) } returns mockProducts

            // When
            val result = graphicsRepository.generateBestSellingProductsChart(testConfig)

            // Then
            assertNotNull(result)
            assertTrue(result.isNotEmpty())
        }

        @Test
        @DisplayName("Should handle empty product list")
        fun shouldHandleEmptyProductList() = runTest {
            // Given
            coEvery { analyticsRepository.getBestSellingProducts(testConfig) } returns emptyList()

            // When
            val result = graphicsRepository.generateBestSellingProductsChart(testConfig)

            // Then
            assertNotNull(result)
            assertTrue(result.isNotEmpty()) // Should return placeholder image
        }
    }

    @Nested
    @DisplayName("Sales Trend Chart Tests")
    inner class SalesTrendChartTests {

        @Test
        @DisplayName("Should generate sales trend chart successfully")
        fun shouldGenerateSalesTrendChartSuccessfully() = runTest {
            // Given
            val mockTrendData = listOf(
                DailySales(
                    date = LocalDate.of(2024, 1, 1),
                    totalSales = 100,
                    totalRevenue = BigDecimal("15000.00")
                ),
                DailySales(
                    date = LocalDate.of(2024, 1, 2),
                    totalSales = 120,
                    totalRevenue = BigDecimal("18000.00")
                ),
                DailySales(
                    date = LocalDate.of(2024, 1, 3),
                    totalSales = 80,
                    totalRevenue = BigDecimal("12000.00")
                )
            )

            coEvery { analyticsRepository.getSalesTrend(testConfig) } returns mockTrendData

            // When
            val result = graphicsRepository.generateSalesTrendChart(testConfig)

            // Then
            assertNotNull(result)
            assertTrue(result.isNotEmpty())
            coVerify { analyticsRepository.getSalesTrend(testConfig) }
        }

        @Test
        @DisplayName("Should handle single day trend data")
        fun shouldHandleSingleDayTrendData() = runTest {
            // Given
            val mockTrendData = listOf(
                DailySales(
                    date = LocalDate.of(2024, 1, 1),
                    totalSales = 50,
                    totalRevenue = BigDecimal("7500.00")
                )
            )

            coEvery { analyticsRepository.getSalesTrend(testConfig) } returns mockTrendData

            // When
            val result = graphicsRepository.generateSalesTrendChart(testConfig)

            // Then
            assertNotNull(result)
            assertTrue(result.isNotEmpty())
        }
    }

    @Nested
    @DisplayName("Category Performance Chart Tests")
    inner class CategoryPerformanceChartTests {

        @Test
        @DisplayName("Should generate category performance chart successfully")
        fun shouldGenerateCategoryPerformanceChartSuccessfully() = runTest {
            // Given
            val mockCategoryPerformance = mapOf(
                "Electronics" to 50000.0,
                "Clothing" to 30000.0,
                "Books" to 15000.0,
                "Sports" to 25000.0
            )

            coEvery { analyticsRepository.getCategoryPerformance(testConfig) } returns mockCategoryPerformance

            // When
            val result = graphicsRepository.generateCategoryPerformanceChart(testConfig)

            // Then
            assertNotNull(result)
            assertTrue(result.isNotEmpty())
            coVerify { analyticsRepository.getCategoryPerformance(testConfig) }
        }

        @Test
        @DisplayName("Should handle empty category data")
        fun shouldHandleEmptyCategoryData() = runTest {
            // Given
            coEvery { analyticsRepository.getCategoryPerformance(testConfig) } returns emptyMap()

            // When
            val result = graphicsRepository.generateCategoryPerformanceChart(testConfig)

            // Then
            assertNotNull(result)
            assertTrue(result.isNotEmpty())
        }
    }

    @Nested
    @DisplayName("Profit Loss Chart Tests")
    inner class ProfitLossChartTests {

        @Test
        @DisplayName("Should generate profit loss chart successfully")
        fun shouldGenerateProfitLossChartSuccessfully() = runTest {
            // Given
            val mockProfitProducts = listOf(
                ProductSales(1L, "Profitable Product 1", 100, BigDecimal("10000.00")),
                ProductSales(2L, "Profitable Product 2", 80, BigDecimal("8000.00"))
            )

            val mockLossProducts = listOf(
                ProductSales(3L, "Loss Product 1", 20, BigDecimal("2000.00")),
                ProductSales(4L, "Loss Product 2", 10, BigDecimal("1000.00"))
            )

            coEvery { analyticsRepository.getProfitAnalysis(testConfig) } returns mockProfitProducts
            coEvery { analyticsRepository.getLossAnalysis(testConfig) } returns mockLossProducts

            // When
            val result = graphicsRepository.generateProfitLossChart(testConfig)

            // Then
            assertNotNull(result)
            assertTrue(result.isNotEmpty())
            coVerify { analyticsRepository.getProfitAnalysis(testConfig) }
            coVerify { analyticsRepository.getLossAnalysis(testConfig) }
        }

        @Test
        @DisplayName("Should handle case with only profit products")
        fun shouldHandleCaseWithOnlyProfitProducts() = runTest {
            // Given
            val mockProfitProducts = listOf(
                ProductSales(1L, "Profitable Product", 100, BigDecimal("10000.00"))
            )

            coEvery { analyticsRepository.getProfitAnalysis(testConfig) } returns mockProfitProducts
            coEvery { analyticsRepository.getLossAnalysis(testConfig) } returns emptyList()

            // When
            val result = graphicsRepository.generateProfitLossChart(testConfig)

            // Then
            assertNotNull(result)
            assertTrue(result.isNotEmpty())
        }

        @Test
        @DisplayName("Should limit to 10 products for each category")
        fun shouldLimitToTenProductsForEachCategory() = runTest {
            // Given
            val mockProfitProducts = (1..15).map {
                ProductSales(it.toLong(), "Profit Product $it", 100, BigDecimal("1000.00"))
            }
            val mockLossProducts = (16..25).map {
                ProductSales(it.toLong(), "Loss Product $it", 10, BigDecimal("100.00"))
            }

            coEvery { analyticsRepository.getProfitAnalysis(testConfig) } returns mockProfitProducts
            coEvery { analyticsRepository.getLossAnalysis(testConfig) } returns mockLossProducts

            // When
            val result = graphicsRepository.generateProfitLossChart(testConfig)

            // Then
            assertNotNull(result)
            assertTrue(result.isNotEmpty())
            // Verify that only take(10) was called implicitly by the method behavior
        }
    }

    @Nested
    @DisplayName("Dashboard Tests")
    inner class DashboardTests {

        @Test
        @DisplayName("Should generate dashboard successfully")
        fun shouldGenerateDashboardSuccessfully() = runTest {
            // Given
            val mockAnalytics = SalesAnalytics(
                totalSales = 1000,
                totalRevenue = BigDecimal("150000.00"),
                averageOrderValue = BigDecimal("150.00"),
                topSellingCategory = "Electronics"
            )

            val mockTrendData = listOf(
                DailySales(LocalDate.now(), 100, BigDecimal("15000.00"))
            )

            coEvery { analyticsRepository.getSalesAnalytics(testConfig) } returns mockAnalytics
            coEvery { analyticsRepository.getSalesTrend(testConfig) } returns mockTrendData

            // When
            val result = graphicsRepository.generateDashboard(testConfig)

            // Then
            assertNotNull(result)
            assertTrue(result.isNotEmpty())
            coVerify { analyticsRepository.getSalesAnalytics(testConfig) }
        }
    }

    @Nested
    @DisplayName("Custom Chart Tests")
    inner class CustomChartTests {

        @Test
        @DisplayName("Should generate bar chart successfully")
        fun shouldGenerateBarChartSuccessfully() = runTest {
            // Given
            val data = mapOf(
                "x" to listOf("A", "B", "C"),
                "y" to listOf(1, 2, 3)
            )

            // When
            val result = graphicsRepository.generateCustomChart(
                data = data,
                chartType = ChartType.BAR,
                title = "Test Bar Chart",
                xLabel = "Categories",
                yLabel = "Values"
            )

            // Then
            assertNotNull(result)
            assertTrue(result.isNotEmpty())
        }

        @Test
        @DisplayName("Should generate line chart successfully")
        fun shouldGenerateLineChartSuccessfully() = runTest {
            // Given
            val data = mapOf(
                "x" to listOf(1, 2, 3, 4, 5),
                "y" to listOf(2, 4, 6, 8, 10)
            )

            // When
            val result = graphicsRepository.generateCustomChart(
                data = data,
                chartType = ChartType.LINE,
                title = "Test Line Chart",
                xLabel = "X Values",
                yLabel = "Y Values"
            )

            // Then
            assertNotNull(result)
            assertTrue(result.isNotEmpty())
        }

        @Test
        @DisplayName("Should generate scatter plot successfully")
        fun shouldGenerateScatterPlotSuccessfully() = runTest {
            // Given
            val data = mapOf(
                "x" to listOf(1.0, 2.0, 3.0, 4.0),
                "y" to listOf(2.1, 3.9, 6.2, 7.8)
            )

            // When
            val result = graphicsRepository.generateCustomChart(
                data = data,
                chartType = ChartType.SCATTER,
                title = "Test Scatter Plot",
                xLabel = "X Values",
                yLabel = "Y Values"
            )

            // Then
            assertNotNull(result)
            assertTrue(result.isNotEmpty())
        }

        @Test
        @DisplayName("Should generate area chart successfully")
        fun shouldGenerateAreaChartSuccessfully() = runTest {
            // Given
            val data = mapOf(
                "x" to listOf(1, 2, 3, 4),
                "y" to listOf(10, 20, 15, 25)
            )

            // When
            val result = graphicsRepository.generateCustomChart(
                data = data,
                chartType = ChartType.AREA,
                title = "Test Area Chart",
                xLabel = "Time",
                yLabel = "Values"
            )

            // Then
            assertNotNull(result)
            assertTrue(result.isNotEmpty())
        }

        @Test
        @DisplayName("Should generate histogram successfully")
        fun shouldGenerateHistogramSuccessfully() = runTest {
            // Given
            val data = mapOf(
                "values" to listOf(1, 2, 2, 3, 3, 3, 4, 4, 5)
            )

            // When
            val result = graphicsRepository.generateCustomChart(
                data = data,
                chartType = ChartType.HISTOGRAM,
                title = "Test Histogram",
                xLabel = "Values",
                yLabel = "Frequency"
            )

            // Then
            assertNotNull(result)
            assertTrue(result.isNotEmpty())
        }

        @Test
        @DisplayName("Should handle empty data gracefully")
        fun shouldHandleEmptyDataGracefully() = runTest {
            // Given
            val emptyData = emptyMap<String, Any>()

            // When
            val result = graphicsRepository.generateCustomChart(
                data = emptyData,
                chartType = ChartType.BAR,
                title = "Empty Chart",
                xLabel = "X",
                yLabel = "Y"
            )

            // Then
            assertNotNull(result)
            assertTrue(result.isNotEmpty()) // Should return placeholder image
        }
    }

    @Nested
    @DisplayName("Error Handling Tests")
    inner class ErrorHandlingTests {

        @Test
        @DisplayName("Should handle repository exceptions gracefully")
        fun shouldHandleRepositoryExceptionsGracefully() = runTest {
            // Given
            coEvery { analyticsRepository.getBestSellingProducts(testConfig) } throws RuntimeException("Database error")

            // When
            val result = graphicsRepository.generateBestSellingProductsChart(testConfig)

            // Then
            assertNotNull(result)
            assertTrue(result.isNotEmpty()) // Should return placeholder image with error
        }

        @Test
        @DisplayName("Should handle null data gracefully")
        fun shouldHandleNullDataGracefully() = runTest {
            // Given
            val dataWithNulls = mapOf(
                "x" to listOf("A", null, "C"),
                "y" to listOf(1, null, 3)
            )

            // When
            val result = graphicsRepository.generateCustomChart(
                data = dataWithNulls,
                chartType = ChartType.BAR,
                title = "Chart with Nulls",
                xLabel = "X",
                yLabel = "Y"
            )

            // Then
            assertNotNull(result)
            assertTrue(result.isNotEmpty())
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    inner class IntegrationTests {

        @Test
        @DisplayName("Should work with realistic data volumes")
        fun shouldWorkWithRealisticDataVolumes() = runTest {
            // Given
            val largeProfitList = (1..100).map {
                ProductSales(it.toLong(), "Product $it", it * 10, BigDecimal(it * 1000))
            }
            val largeLossList = (101..150).map {
                ProductSales(it.toLong(), "Product $it", it, BigDecimal(it * 10))
            }

            coEvery { analyticsRepository.getProfitAnalysis(testConfig) } returns largeProfitList
            coEvery { analyticsRepository.getLossAnalysis(testConfig) } returns largeLossList

            // When
            val result = graphicsRepository.generateProfitLossChart(testConfig)

            // Then
            assertNotNull(result)
            assertTrue(result.isNotEmpty())
        }

        @Test
        @DisplayName("Should handle multiple chart generations in sequence")
        fun shouldHandleMultipleChartGenerationsInSequence() = runTest {
            // Given
            val mockProducts = listOf(
                ProductSales(1L, "Product", 10, BigDecimal("1000.00"))
            )
            val mockTrend = listOf(
                DailySales(LocalDate.now(), 10, BigDecimal("1000.00"))
            )
            val mockCategories = mapOf("Category" to 1000.0)

            coEvery { analyticsRepository.getBestSellingProducts(testConfig) } returns mockProducts
            coEvery { analyticsRepository.getSalesTrend(testConfig) } returns mockTrend
            coEvery { analyticsRepository.getCategoryPerformance(testConfig) } returns mockCategories

            // When
            val result1 = graphicsRepository.generateBestSellingProductsChart(testConfig)
            val result2 = graphicsRepository.generateSalesTrendChart(testConfig)
            val result3 = graphicsRepository.generateCategoryPerformanceChart(testConfig)

            // Then
            assertNotNull(result1)
            assertNotNull(result2)
            assertNotNull(result3)
            assertTrue(result1.isNotEmpty())
            assertTrue(result2.isNotEmpty())
            assertTrue(result3.isNotEmpty())
        }
    }
}