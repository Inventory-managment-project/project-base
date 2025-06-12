package model.store.sales

import kotlinx.coroutines.runBlocking
import model.store.sales.repository.DBSalesRepository
import mx.unam.fciencias.ids.eq1.db.store.StoreTable
import mx.unam.fciencias.ids.eq1.db.store.product.ProductPriceTable
import mx.unam.fciencias.ids.eq1.db.store.product.ProductTable
import mx.unam.fciencias.ids.eq1.db.store.sales.SalesTable
import mx.unam.fciencias.ids.eq1.db.store.sales.SalesDetailsTable
import mx.unam.fciencias.ids.eq1.db.user.UserTable
import mx.unam.fciencias.ids.eq1.model.store.CreateStoreRequest
import mx.unam.fciencias.ids.eq1.model.store.product.Product
import mx.unam.fciencias.ids.eq1.model.store.sales.Sales
import mx.unam.fciencias.ids.eq1.model.store.product.repository.DBProductRepository
import mx.unam.fciencias.ids.eq1.model.store.repository.DBStoreRepository
import mx.unam.fciencias.ids.eq1.model.user.User
import mx.unam.fciencias.ids.eq1.model.user.repository.DBUserRepository
import mx.unam.fciencias.ids.eq1.model.user.repository.UserRepository
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import java.math.BigDecimal
import java.time.Instant
import kotlin.test.*

class DBSalesRepositoryTest {

    private lateinit var database: Database
    private lateinit var saleRepository: DBSalesRepository
    private lateinit var productRepository: DBProductRepository
    private lateinit var storeRepository: DBStoreRepository
    private lateinit var userRepository:  UserRepository

    private val users = listOf(
        User(
            id = 1 ,
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

    private val products = listOf(
        Product(
            1,
            "Product 1",
            "Description 1",
            BigDecimal.valueOf(20.0),
            "1234567890",
            BigDecimal.valueOf(15.0),
            BigDecimal.valueOf(20.0),
            0L,
            BigDecimal(100),
            0,
            1
        ),
        Product(
            2,
            "Product 2",
            "Description 2",
            BigDecimal.valueOf(30.0),
            "0987654321",
            BigDecimal.valueOf(25.0),
            BigDecimal.valueOf(30.0),
            0L,
            BigDecimal(50),
            0,
            1
        )
    )

    @BeforeEach
    fun setUp() {
        database = Database.connect(
            url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
            driver = "org.h2.Driver"
        )

        transaction(database) {
            SchemaUtils.create(UserTable)
            SchemaUtils.create(StoreTable)
            SchemaUtils.create(ProductTable)
            SchemaUtils.create(ProductPriceTable)
            SchemaUtils.create(SalesTable)
            SchemaUtils.create(SalesDetailsTable)
        }

        userRepository = DBUserRepository(database)
        storeRepository = DBStoreRepository(database)
        productRepository = DBProductRepository(database, storeID = 1)
        saleRepository = DBSalesRepository(database, storeID = 1)

        runBlocking {
            users.forEach { userRepository.add(it) }
            stores.forEachIndexed { index, store -> storeRepository.add(store, users[index]) }
            products.forEach { productRepository.add(it) }
        }

        startKoin {
            modules(
                module {
                    single { saleRepository }
                    single { productRepository }
                }
            )
        }
    }

    @AfterEach
    fun tearDown() {
        transaction(database) {
            SchemaUtils.drop(SalesDetailsTable)
            SchemaUtils.drop(SalesTable)
            SchemaUtils.drop(ProductPriceTable)
            SchemaUtils.drop(ProductTable)
            SchemaUtils.drop(StoreTable)
            SchemaUtils.drop(UserTable)
        }
        stopKoin()
    }

    @Test
    fun `test add and retrieve sale`() = runBlocking {
        val sales = Sales(
            id = 0, // Use 0 for new sales
            products = listOf(Pair(1, BigDecimal(2)), Pair(2, BigDecimal(1))),
            paymentmethod = "CASH",
            total = BigDecimal(70.0),
            created = Instant.now().epochSecond,
            subtotal = BigDecimal(70.0)
        )

        val result = saleRepository.add(sales)
        assertTrue(result != -1, "Sale should be added successfully, but got: $result")

        val savedSale = saleRepository.getById(result)
        assertNotNull(savedSale, "Saved sale should not be null")
        assertEquals("CASH", savedSale.paymentmethod, "Payment method should match")
        assertEquals(2, savedSale.products.size, "Should have 2 products, but got: ${savedSale.products.size}")

        // Debug info
        println("Added sale with ID: $result")
        println("Retrieved sale: $savedSale")
    }

    @Test
    fun `test getById non-existent sale`() = runBlocking {
        val nonExistentSale = saleRepository.getById(999)
        assertNull(nonExistentSale)
    }

    @Test
    fun `test add sale updates product stock`() = runBlocking {
        val initialProduct = productRepository.getById(1)
        assertNotNull(initialProduct, "Initial product should exist")
        val initialStock = initialProduct.stock
        println("Initial stock for product 1: $initialStock")

        val sale = Sales(
            id = 0,
            products = listOf(Pair(1, BigDecimal(5))),
            paymentmethod = "CASH",
            total = BigDecimal(100.0),
            created = Instant.now().epochSecond,
            subtotal = BigDecimal(100.0)
        )

        val result = saleRepository.add(sale)
        assertTrue(result != -1, "Sale should be added successfully, but got: $result")

        val updatedProduct = productRepository.getById(1)
        assertNotNull(updatedProduct, "Updated product should exist")
        val expectedStock = initialStock - BigDecimal(5)

        println("Expected stock: $expectedStock")
        println("Actual stock: ${updatedProduct.stock}")

        assertEquals(expectedStock, updatedProduct.stock,
            "Stock should be reduced from $initialStock to $expectedStock, but got: ${updatedProduct.stock}")
    }

    @Test
    fun `test update sale`() = runBlocking {
        // First, add a sale to update
        val originalSale = Sales(
            id = 0,
            products = listOf(Pair(1, BigDecimal(2)), Pair(2, BigDecimal(1))),
            paymentmethod = "CASH",
            total = BigDecimal(70.0),
            created = Instant.now().epochSecond,
            subtotal = BigDecimal(70.0)
        )

        val saleId = saleRepository.add(originalSale)
        assertTrue(saleId != -1, "Original sale should be added successfully")

        // Now update the sale with different products and payment method
        val updatedSale = Sales(
            id = saleId,
            products = listOf(Pair(1, BigDecimal(3)), Pair(2, BigDecimal(2))), // Different quantities
            paymentmethod = "CARD", // Different payment method
            total = BigDecimal(120.0), // This will be recalculated
            created = Instant.now().epochSecond,
            subtotal = BigDecimal(120.0)
        )

        val updateResult = saleRepository.update(updatedSale)
        assertTrue(updateResult, "Sale should be updated successfully")

        // Retrieve the updated sale and verify changes
        val retrievedSale = saleRepository.getById(saleId)
        assertNotNull(retrievedSale, "Updated sale should exist")
        assertEquals("CARD", retrievedSale.paymentmethod, "Payment method should be updated to CARD")
        assertEquals(2, retrievedSale.products.size, "Should still have 2 products")

        // Verify the products and quantities are updated
        val product1Entry = retrievedSale.products.find { it.first == 1 }
        val product2Entry = retrievedSale.products.find { it.first == 2 }

        assertNotNull(product1Entry, "Product 1 should exist in updated sale")
        assertNotNull(product2Entry, "Product 2 should exist in updated sale")

        // Fix: Compare BigDecimal values properly
        assertEquals(0, BigDecimal(3).compareTo(product1Entry.second),
            "Product 1 quantity should be updated to 3, but was: ${product1Entry.second}")
        assertEquals(0, BigDecimal(2).compareTo(product2Entry.second),
            "Product 2 quantity should be updated to 2, but was: ${product2Entry.second}")

        // Verify total is recalculated correctly (Product 1: 20*3=60, Product 2: 30*2=60, Total: 120)
        assertEquals(0, BigDecimal("120.0").compareTo(retrievedSale.total),
            "Total should be recalculated to 120.0, but was: ${retrievedSale.total}")

        println("Updated sale: $retrievedSale")
    }

    @Test
    fun `test update non-existent sale`() = runBlocking {
        val nonExistentSale = Sales(
            id = 999, // Non-existent ID
            products = listOf(Pair(1, BigDecimal(1))),
            paymentmethod = "CASH",
            total = BigDecimal(20.0),
            created = Instant.now().epochSecond,
            subtotal = BigDecimal(20.0)
        )

        val result = saleRepository.update(nonExistentSale)
        assertFalse(result, "Updating non-existent sale should return false")
    }

    @Test
    fun `test update sale with invalid product`() = runBlocking {
        // First add a valid sale
        val originalSale = Sales(
            id = 0,
            products = listOf(Pair(1, BigDecimal(1))),
            paymentmethod = "CASH",
            total = BigDecimal(20.0),
            created = Instant.now().epochSecond,
            subtotal = BigDecimal(20.0)
        )

        val saleId = saleRepository.add(originalSale)
        assertTrue(saleId != -1, "Original sale should be added successfully")

        // Try to update with non-existent product
        val updatedSale = Sales(
            id = saleId,
            products = listOf(Pair(999, BigDecimal(1))), // Non-existent product ID
            paymentmethod = "CARD",
            total = BigDecimal(0.0),
            created = Instant.now().epochSecond,
            subtotal = BigDecimal(0.0)
        )

        val updateResult = saleRepository.update(updatedSale)
        assertTrue(updateResult, "Update should succeed even with invalid products")

        // Verify the sale was updated but with empty products
        val retrievedSale = saleRepository.getById(saleId)
        assertNotNull(retrievedSale, "Sale should still exist")
        assertEquals("CARD", retrievedSale.paymentmethod, "Payment method should be updated")
        assertEquals(0, retrievedSale.products.size, "Should have no products due to invalid product ID")

        // Fix: Compare BigDecimal values properly for zero
        assertEquals(0, BigDecimal.ZERO.compareTo(retrievedSale.total),
            "Total should be 0 with no valid products, but was: ${retrievedSale.total}")
    }
}
