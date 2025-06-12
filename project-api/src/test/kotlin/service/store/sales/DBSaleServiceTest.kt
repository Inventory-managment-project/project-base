package service.store.sales

import kotlinx.coroutines.runBlocking
import model.store.sale.repository.DBSalesRepository
import mx.unam.fciencias.ids.eq1.db.store.sales.PAYMENTMETHOD
import mx.unam.fciencias.ids.eq1.model.store.CreateStoreRequest
import mx.unam.fciencias.ids.eq1.model.store.product.coupon.CreateCouponRequest
import mx.unam.fciencias.ids.eq1.model.store.product.coupon.DBCouponsRepository
import mx.unam.fciencias.ids.eq1.model.store.repository.DBStoreRepository
import mx.unam.fciencias.ids.eq1.model.store.sales.Sale
import mx.unam.fciencias.ids.eq1.model.user.User
import mx.unam.fciencias.ids.eq1.model.user.repository.DBUserRepository
import mx.unam.fciencias.ids.eq1.model.user.repository.UserRepository
import mx.unam.fciencias.ids.eq1.service.store.sales.DBSaleService
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import java.math.BigDecimal
import java.time.Instant
import java.util.*
import kotlin.test.*

class DBSaleServiceTest {

    private lateinit var database: Database
    private lateinit var saleService: DBSaleService
    private lateinit var salesRepository: DBSalesRepository
    private lateinit var couponsRepository: DBCouponsRepository
    private lateinit var storeRepository: DBStoreRepository
    private lateinit var userRepository: UserRepository

    private val storeId = 1
    private val dbName = "test-sales-service-${UUID.randomUUID()}"

    private val testUser = User(
        id = 1,
        name = "testUser",
        email = "test@test.com",
        hashedPassword = "testPassword",
        salt = "salt",
        createdAt = Instant.now().epochSecond
    )

    private val testStore = CreateStoreRequest(
        name = "testStore",
        address = "testAddress"
    )

    private val testSale = Sale(
        id = 1,
        products = listOf(
            Pair(1, 2.toBigDecimal()),
            Pair(2, 1.toBigDecimal()),
        ),
        total = BigDecimal("100.00"),
        paymentmethod = PAYMENTMETHOD.CASH,
        created = Instant.now().epochSecond,
        subtotal = 100.toBigDecimal(),
    )

    @BeforeEach
    fun setUp() {
        database = Database.connect(
            url = "jdbc:h2:mem:$dbName;DB_CLOSE_DELAY=-1",
            driver = "org.h2.Driver"
        )

        userRepository = DBUserRepository(database)
        storeRepository = DBStoreRepository(database)
        salesRepository = DBSalesRepository(database, storeId)
        couponsRepository = DBCouponsRepository(database, storeId)
        saleService = DBSaleService(storeId)

        // Setup test data
        runBlocking {
            userRepository.add(testUser)
            storeRepository.add(testStore, testUser)
        }

        startKoin {
            modules(
                module {
                    single { salesRepository }
                    single { couponsRepository }
                    factory { (_: Int) -> salesRepository }
                    factory { (_: Int) -> couponsRepository }
                }
            )
        }
    }

    @AfterEach
    fun tearDown() {
        transaction(database) {
            exec("DROP ALL OBJECTS")
        }
        stopKoin()
    }

    @Test
    fun `test add sale without coupon`() = runBlocking {
        val saleId = saleService.addSale(testSale)
        assertTrue(saleId > 0, "Sale should be added successfully")

        val retrievedSale = saleService.getSaleById(saleId)
        assertNotNull(retrievedSale)
        assertEquals(testSale.total, retrievedSale.total)
    }

    @Test
    fun `test add sale with percentage coupon`() = runBlocking {
        // Create a percentage coupon
        val couponRequest = CreateCouponRequest(
            couponCode = "SAVE20",
            description = "20% off all items",
            category = "general",
            discount = BigDecimal("20.00"),
            discountAmount = null
        )
        couponsRepository.add(couponRequest)

        val saleId = saleService.addSaleWithCoupon(testSale, "SAVE20")
        assertTrue(saleId > 0, "Sale with coupon should be added successfully")

        val retrievedSale = saleService.getSaleById(saleId)
        assertNotNull(retrievedSale)

        val expectedTotal = BigDecimal("80.00")

        assertEquals(expectedTotal, retrievedSale.total)
    }

    @Test
    fun `test add sale with fixed amount coupon`() = runBlocking {
        // Create a fixed amount coupon
        val couponRequest = CreateCouponRequest(
            couponCode = "SAVE15",
            description = "$15 off purchase",
            category = "amount",
            discount = null,
            discountAmount = BigDecimal("15.00")
        )
        couponsRepository.add(couponRequest)

        val saleId = saleService.addSaleWithCoupon(testSale, "SAVE15")
        assertTrue(saleId > 0, "Sale with fixed amount coupon should be added successfully")

        val retrievedSale = saleService.getSaleById(saleId)
        assertNotNull(retrievedSale)

        val expectedTotal = BigDecimal("85.00")

        assertEquals(expectedTotal, retrievedSale.total)
    }

    @Test
    fun `test add sale with invalid coupon`() = runBlocking {
        val saleId = saleService.addSaleWithCoupon(testSale, "INVALID_COUPON")
        assertTrue(saleId > 0, "Sale should still be added even with invalid coupon")

        val retrievedSale = saleService.getSaleById(saleId)
        assertNotNull(retrievedSale)
        assertEquals(testSale.total, retrievedSale.total)
    }

    @Test
    fun `test add sale with expired coupon`() = runBlocking {
        val now = Instant.now()
        val expiredCoupon = CreateCouponRequest(
            couponCode = "EXPIRED",
            description = "Expired coupon",
            discount = BigDecimal("10.00"),
            validFrom = now.minusSeconds(7200).epochSecond, // 2 hours ago
            validUntil = now.minusSeconds(3600).epochSecond  // 1 hour ago (expired)
        )
        couponsRepository.add(expiredCoupon)

        val saleId = saleService.addSaleWithCoupon(testSale, "EXPIRED")
        assertTrue(saleId > 0, "Sale should still be added")

        val retrievedSale = saleService.getSaleById(saleId)
        assertNotNull(retrievedSale)
        // Should be original sale without discount since coupon is expired
        assertEquals(testSale.total, retrievedSale.total)
    }

    @Test
    fun `test validate coupon for sale`() = runBlocking {
        val validCoupon = CreateCouponRequest(
            couponCode = "VALID_COUPON",
            description = "Valid coupon",
            discount = BigDecimal("10.00")
        )
        couponsRepository.add(validCoupon)

        assertTrue(saleService.validateCouponForSale("VALID_COUPON", testSale))
        assertFalse(saleService.validateCouponForSale("INVALID_COUPON", testSale))
    }

    @Test
    fun `test get applicable coupons for sale`() = runBlocking {
        // Add various coupons
        val generalCoupon = CreateCouponRequest(
            couponCode = "GENERAL",
            description = "General coupon",
            discount = BigDecimal("10.00")
        )

        val productSpecificCoupon = CreateCouponRequest(
            couponCode = "PRODUCT_SPECIFIC",
            description = "Product specific coupon",
            discount = BigDecimal("15.00"),
            prodId = 1 // Assuming product ID 1 is in our test sale
        )

        val expiredCoupon = CreateCouponRequest(
            couponCode = "EXPIRED_GENERAL",
            description = "Expired general coupon",
            discount = BigDecimal("20.00"),
            validFrom = Instant.now().minusSeconds(7200).epochSecond,
            validUntil = Instant.now().minusSeconds(3600).epochSecond
        )

        couponsRepository.add(generalCoupon)
        couponsRepository.add(productSpecificCoupon)
        couponsRepository.add(expiredCoupon)

        val applicableCoupons = saleService.getApplicableCoupons(testSale)

        // Should only get valid coupons (general and product-specific for product 1)
        assertEquals(2, applicableCoupons.size)
        assertTrue(applicableCoupons.any { it.couponCode == "GENERAL" })
        assertTrue(applicableCoupons.any { it.couponCode == "PRODUCT_SPECIFIC" })
        assertFalse(applicableCoupons.any { it.couponCode == "EXPIRED_GENERAL" })
    }

    @Test
    fun `test get coupons for products`() = runBlocking {
        val productCoupon1 = CreateCouponRequest(
            couponCode = "PRODUCT1_COUPON",
            description = "Coupon for product 1",
            discount = BigDecimal("10.00"),
            prodId = 1
        )

        val productCoupon2 = CreateCouponRequest(
            couponCode = "PRODUCT2_COUPON",
            description = "Coupon for product 2",
            discount = BigDecimal("15.00"),
            prodId = 2
        )

        val generalCoupon = CreateCouponRequest(
            couponCode = "GENERAL_COUPON",
            description = "General coupon",
            discount = BigDecimal("5.00")
        )

        couponsRepository.add(productCoupon1)
        couponsRepository.add(productCoupon2)
        couponsRepository.add(generalCoupon)

        val productIds = listOf(1, 2)
        val coupons = saleService.getCouponsForProducts(productIds)

        assertEquals(3, coupons.size)
        assertTrue(coupons.any { it.couponCode == "PRODUCT1_COUPON" })
        assertTrue(coupons.any { it.couponCode == "PRODUCT2_COUPON" })
        assertTrue(coupons.any { it.couponCode == "GENERAL_COUPON" })
    }

    @Test
    fun `test apply coupon to sale with percentage discount`() = runBlocking {
        val couponRequest = CreateCouponRequest(
            couponCode = "PERCENT_TEST",
            description = "Test percentage coupon",
            discount = BigDecimal("25.00")
        )
        couponsRepository.add(couponRequest)

        val discountedSale = saleService.applyCouponToSale(testSale, "PERCENT_TEST")
        assertNotNull(discountedSale)

        val expectedDiscount = BigDecimal("25.00") // 25% of 100.00
        val expectedTotal = BigDecimal("75.00")

        assertEquals(expectedTotal, discountedSale.total)
    }

    @Test
    fun `test apply coupon to sale with fixed amount discount`() = runBlocking {
        val couponRequest = CreateCouponRequest(
            couponCode = "AMOUNT_TEST",
            description = "Test amount coupon",
            discountAmount = BigDecimal("30.00")
        )
        couponsRepository.add(couponRequest)

        val discountedSale = saleService.applyCouponToSale(testSale, "AMOUNT_TEST")
        assertNotNull(discountedSale)

        val expectedDiscount = BigDecimal("30.00")
        val expectedTotal = BigDecimal("70.00")

        assertEquals(expectedTotal, discountedSale.total)
    }

    @Test
    fun `test apply coupon with discount exceeding sale total`() = runBlocking {
        val largeCoupon = CreateCouponRequest(
            couponCode = "BIG_DISCOUNT",
            description = "Large discount coupon",
            discountAmount = BigDecimal("150.00") // More than sale total
        )
        couponsRepository.add(largeCoupon)

        val discountedSale = saleService.applyCouponToSale(testSale, "BIG_DISCOUNT")
        assertNotNull(discountedSale)

        assertEquals(BigDecimal.ZERO, discountedSale.total)
    }

    @Test
    fun `test basic sale service operations`() = runBlocking {
        // Test basic operations still work
        val saleId = saleService.addSale(testSale)
        assertTrue(saleId > 0)

        val retrievedSale = saleService.getSaleById(saleId)
        assertNotNull(retrievedSale)

        val allSales = saleService.getAllSales()
        assertEquals(1, allSales.size)

        val salesByPayment = saleService.getSalesByPaymentMethod(PAYMENTMETHOD.CASH)
        assertEquals(1, salesByPayment.size)

        val deleted = saleService.deleteSale(saleId)
        assertTrue(deleted)

        val deletedSale = saleService.getSaleById(saleId)
        assertNull(deletedSale)
    }
}