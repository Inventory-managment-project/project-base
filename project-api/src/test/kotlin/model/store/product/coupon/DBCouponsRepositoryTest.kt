package model.store.product.coupon

import mx.unam.fciencias.ids.eq1.model.store.CreateStoreRequest
import mx.unam.fciencias.ids.eq1.model.store.product.coupon.DBCouponsRepository
import mx.unam.fciencias.ids.eq1.model.store.repository.DBStoreRepository
import mx.unam.fciencias.ids.eq1.model.user.User
import mx.unam.fciencias.ids.eq1.model.user.repository.DBUserRepository
import mx.unam.fciencias.ids.eq1.model.user.repository.UserRepository
import org.junit.jupiter.api.BeforeEach
import java.time.Instant
import java.util.UUID
import kotlinx.coroutines.runBlocking
import mx.unam.fciencias.ids.eq1.model.store.product.coupon.CreateCouponRequest
import mx.unam.fciencias.ids.eq1.model.store.product.coupon.UpdateCouponRequest
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import java.math.BigDecimal
import kotlin.test.*


class DBCouponsRepositoryTest {

    private lateinit var database: Database
    private lateinit var couponsRepository: DBCouponsRepository
    private lateinit var storeRepository: DBStoreRepository
    private lateinit var userRepository: UserRepository
    private val storeId = 1
    val dbName = "test-coupons-${UUID.randomUUID()}"

    private val users = listOf(
        User(
            id = 1,
            name = "testUser",
            email = "test@test.com",
            hashedPassword = "testPassword",
            salt = "salt",
            createdAt = Instant.now().epochSecond
        )
    )

    private val stores = listOf(
        CreateStoreRequest(
            name = "testStore",
            address = "testAddress",
        )
    )

    @BeforeEach
    fun setUp() {
        database = Database.connect(
            url = "jdbc:h2:mem:$dbName;DB_CLOSE_DELAY=-1",
            driver = "org.h2.Driver"
        )

        userRepository = DBUserRepository(database)
        storeRepository = DBStoreRepository(database)
        couponsRepository = DBCouponsRepository(database, storeId)

        runBlocking {
            users.forEach { userRepository.add(it) }
            stores.forEachIndexed { index, store -> storeRepository.add(store, users[index]) }
        }

        startKoin {
            modules(
                module {
                    single { couponsRepository }
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
    fun `test add and retrieve percentage coupon`() = runBlocking {
        val couponRequest = CreateCouponRequest(
            couponCode = "SAVE20",
            description = "20% off all items",
            category = "general",
            discount = BigDecimal("20.00"),
            discountAmount = null
        )

        val result = couponsRepository.add(couponRequest)
        assertTrue(result, "Coupon should be added successfully")

        val retrievedCoupon = couponsRepository.getById("SAVE20")
        assertNotNull(retrievedCoupon)
        assertEquals("SAVE20", retrievedCoupon.couponCode)
        assertEquals("20% off all items", retrievedCoupon.description)
        assertEquals(BigDecimal("20.00"), retrievedCoupon.discount)
        assertNull(retrievedCoupon.discountAmount)
    }

    @Test
    fun `test add and retrieve amount coupon`() = runBlocking {
        val couponRequest = CreateCouponRequest(
            couponCode = "SAVE10BUCKS",
            description = "$10 off purchase",
            category = "amount",
            discount = null,
            discountAmount = BigDecimal("10.00")
        )

        val result = couponsRepository.add(couponRequest)
        assertTrue(result, "Amount coupon should be added successfully")

        val retrievedCoupon = couponsRepository.getById("SAVE10BUCKS")
        assertNotNull(retrievedCoupon)
        assertEquals("SAVE10BUCKS", retrievedCoupon.couponCode)
        assertNull(retrievedCoupon.discount)
        assertEquals(BigDecimal("10.00"), retrievedCoupon.discountAmount)
    }

    @Test
    fun `test constraint validation - both discount types provided`() = runBlocking {
        val invalidCouponRequest = CreateCouponRequest(
            couponCode = "INVALID",
            description = "Invalid coupon",
            discount = BigDecimal("20.00"),
            discountAmount = BigDecimal("10.00") // Both provided - should fail
        )

        val result = couponsRepository.add(invalidCouponRequest)
        assertFalse(result, "Should fail when both discount types are provided")
    }

    @Test
    fun `test constraint validation - no discount types provided`() = runBlocking {
        val invalidCouponRequest = CreateCouponRequest(
            couponCode = "NODISCOUNT",
            description = "No discount coupon",
            discount = null,
            discountAmount = null // Both null - should fail
        )

        val result = couponsRepository.add(invalidCouponRequest)
        assertFalse(result, "Should fail when neither discount type is provided")
    }

    @Test
    fun `test update coupon`() = runBlocking {
        val originalCoupon = CreateCouponRequest(
            couponCode = "UPDATE_ME",
            description = "Original description",
            category = "original",
            discount = BigDecimal("15.00")
        )

        couponsRepository.add(originalCoupon)

        val updateRequest = UpdateCouponRequest(
            description = "Updated description",
            category = "updated",
            discount = BigDecimal("25.00")
        )

        val updateResult = couponsRepository.update("UPDATE_ME", updateRequest)
        assertTrue(updateResult, "Update should succeed")

        val updatedCoupon = couponsRepository.getById("UPDATE_ME")
        assertNotNull(updatedCoupon)
        assertEquals("Updated description", updatedCoupon.description)
        assertEquals("updated", updatedCoupon.category)
        assertEquals(BigDecimal("25.00"), updatedCoupon.discount)
    }

    @Test
    fun `test delete coupon`() = runBlocking {
        val couponRequest = CreateCouponRequest(
            couponCode = "DELETE_ME",
            description = "To be deleted",
            discount = BigDecimal("10.00")
        )

        couponsRepository.add(couponRequest)
        assertTrue(couponsRepository.existsById("DELETE_ME"))

        val deleteResult = couponsRepository.delete("DELETE_ME")
        assertTrue(deleteResult, "Delete should succeed")

        assertFalse(couponsRepository.existsById("DELETE_ME"))
        assertNull(couponsRepository.getById("DELETE_ME"))
    }

    @Test
    fun `test get coupons by category`() = runBlocking {
        val coupon1 = CreateCouponRequest("ELECTRONICS1", "Electronics discount", "electronics", BigDecimal("15.00"))
        val coupon2 = CreateCouponRequest("ELECTRONICS2", "Another electronics discount", "electronics", BigDecimal("20.00"))
        val coupon3 = CreateCouponRequest("CLOTHING1", "Clothing discount", "clothing", BigDecimal("10.00"))

        couponsRepository.add(coupon1)
        couponsRepository.add(coupon2)
        couponsRepository.add(coupon3)

        val electronicsCoupons = couponsRepository.getByCategory("electronics")
        assertEquals(2, electronicsCoupons.size)
        assertTrue(electronicsCoupons.any { it.couponCode == "ELECTRONICS1" })
        assertTrue(electronicsCoupons.any { it.couponCode == "ELECTRONICS2" })

        val clothingCoupons = couponsRepository.getByCategory("clothing")
        assertEquals(1, clothingCoupons.size)
        assertEquals("CLOTHING1", clothingCoupons[0].couponCode)
    }

    @Test
    fun `test get valid coupons`() = runBlocking {
        val now = Instant.now()
        val validCoupon = CreateCouponRequest(
            couponCode = "VALID_NOW",
            description = "Valid coupon",
            discount = BigDecimal("10.00"),
            validFrom = now.minusSeconds(3600).epochSecond, // 1 hour ago
            validUntil = now.plusSeconds(3600).epochSecond   // 1 hour from now
        )

        val expiredCoupon = CreateCouponRequest(
            couponCode = "EXPIRED",
            description = "Expired coupon",
            discount = BigDecimal("10.00"),
            validFrom = now.minusSeconds(7200).epochSecond, // 2 hours ago
            validUntil = now.minusSeconds(3600).epochSecond  // 1 hour ago (expired)
        )

        couponsRepository.add(validCoupon)
        couponsRepository.add(expiredCoupon)

        val validCoupons = couponsRepository.getValidCoupons()
        assertEquals(1, validCoupons.size)
        assertEquals("VALID_NOW", validCoupons[0].couponCode)

        assertTrue(couponsRepository.isValidCoupon("VALID_NOW"))
        assertFalse(couponsRepository.isValidCoupon("EXPIRED"))
    }

    @Test
    fun `test get percentage and amount coupons`() = runBlocking {
        val percentageCoupon = CreateCouponRequest("PERCENT1", "Percentage", discount = BigDecimal("15.00"))
        val amountCoupon = CreateCouponRequest("AMOUNT1", "Amount", discountAmount = BigDecimal("5.00"))

        couponsRepository.add(percentageCoupon)
        couponsRepository.add(amountCoupon)

        val percentageCoupons = couponsRepository.getPercentageCoupons()
        assertEquals(1, percentageCoupons.size)
        assertEquals("PERCENT1", percentageCoupons[0].couponCode)

        val amountCoupons = couponsRepository.getAmountCoupons()
        assertEquals(1, amountCoupons.size)
        assertEquals("AMOUNT1", amountCoupons[0].couponCode)
    }

    @Test
    fun `test get all coupons`() = runBlocking {
        val coupon1 = CreateCouponRequest("COUPON1", "First coupon", discount = BigDecimal("10.00"))
        val coupon2 = CreateCouponRequest("COUPON2", "Second coupon", discountAmount = BigDecimal("5.00"))

        couponsRepository.add(coupon1)
        couponsRepository.add(coupon2)

        val allCoupons = couponsRepository.getAll()
        assertEquals(2, allCoupons.size)
        assertTrue(allCoupons.any { it.couponCode == "COUPON1" })
        assertTrue(allCoupons.any { it.couponCode == "COUPON2" })
    }
}