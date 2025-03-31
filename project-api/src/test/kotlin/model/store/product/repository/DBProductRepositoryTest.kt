package model.store.product.repository

import kotlinx.coroutines.runBlocking
import mx.unam.fciencias.ids.eq1.db.store.StoreTable
import mx.unam.fciencias.ids.eq1.db.store.product.ProductPriceTable
import mx.unam.fciencias.ids.eq1.db.store.product.ProductTable
import mx.unam.fciencias.ids.eq1.db.user.UserTable
import mx.unam.fciencias.ids.eq1.model.store.CreateStoreRequest
import mx.unam.fciencias.ids.eq1.model.store.product.Product
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

class DBProductRepositoryTest {

    private lateinit var database: Database
    private lateinit var productRepository: DBProductRepository
    private lateinit var storeRepository: DBStoreRepository
    private lateinit var userRepository: UserRepository

    private val users = listOf(
        User(
            id = 1,
            name = "testUser",
            email = "test@test.com",
            hashedPassword = "testPassword",
            salt = "salt",
            createdAt = Instant.now().epochSecond
        ),
        User(
            id = 2,
            name = "testUser2",
            email = "test2@test.com",
            hashedPassword = "testPassword2",
            salt = "salt2",
            createdAt = Instant.now().epochSecond
        )
    )

    private val stores = listOf(
        CreateStoreRequest(
            name = "testStore",
            address = "testAddress",
        ),
        CreateStoreRequest(
            name = "testStore2",
            address = "testAddress2",
        )
    )

    @BeforeEach
    fun setUp() {
        database = Database.connect(
            url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
            driver = "org.h2.Driver"
        )

        transaction(database) {
            SchemaUtils.create(ProductTable)
        }

        userRepository = DBUserRepository(database)
        storeRepository = DBStoreRepository(database)
        productRepository = DBProductRepository(database, storeID = 1)

        runBlocking {
            users.forEach { userRepository.add(it) }
            stores.forEachIndexed { index, store -> storeRepository.add(store, users[index]) }
        }

        startKoin {
            modules(
                module {
                    single { productRepository }
                }
            )
        }
    }

    @AfterEach
    fun tearDown() {
        transaction(database) {
            SchemaUtils.drop(ProductPriceTable)
            SchemaUtils.drop(ProductTable)
            SchemaUtils.drop(StoreTable)
            SchemaUtils.drop(UserTable)
        }
        stopKoin()
    }

    @Test
    fun `test add and retrieve product`() = runBlocking {
        val product = Product(
            1,
            "Sample Product",
            "Description",
            BigDecimal.valueOf(15.0),
            "",
            BigDecimal.valueOf(15.0),
            BigDecimal.valueOf(15.0),
            0L,
            BigDecimal(100),
            0,
            1
        )
        val result = productRepository.add(product)
        assertTrue(result != -1, "Product should be added successfully")

        val allProducts = productRepository.getAll()
        assertEquals(1, allProducts.size)

        val savedProduct = allProducts[0]
        assertEquals("Sample Product", savedProduct.name)
        assertEquals(BigDecimal("100.0000"), savedProduct.stock)
    }

    @Test
    fun `test getById`() = runBlocking {
        val product = Product(
            0,
            "Unique Product",
            "Test Description",
            BigDecimal.valueOf(15.0),
            "",
            BigDecimal.valueOf(15.0),
            BigDecimal.valueOf(15.0),
            0L,
            BigDecimal(1),
            0,
            1
        )
        productRepository.add(product)

        val foundProduct = productRepository.getById(1)
        assertNotNull(foundProduct)
        assertEquals("Unique Product", foundProduct.name)

        val nonExistentProduct = productRepository.getById(999)
        assertNull(nonExistentProduct)
    }

    @Test
    fun `test update product`() = runBlocking {
        val product = Product(
            1,
            "Old Name",
            "Old Description",
            BigDecimal.valueOf(15.0),
            "",
            BigDecimal.valueOf(15.0),
            BigDecimal.valueOf(15.0),
            0L,
            BigDecimal(1),
            0,
            1
        )
        productRepository.add(product)

        val updatedProduct = product.copy(name = "Updated Name", stock = BigDecimal(40))
        val updateResult = productRepository.update(updatedProduct)
        assertTrue(updateResult, "Update should succeed")

        val retrievedProduct = productRepository.getById(product.id)
        assertNotNull(retrievedProduct)
        assertEquals("Updated Name", retrievedProduct.name)
        assertEquals(BigDecimal("40.0000"), retrievedProduct.stock)
    }

    @Test
    fun `test delete product`() = runBlocking {
        val product = Product(
            0,
            "To Delete",
            "Delete Description",
            BigDecimal.valueOf(15.0),
            "",
            BigDecimal.valueOf(15.0),
            BigDecimal.valueOf(15.0),
            0L,
            BigDecimal(1),
            0,
            1
        )
        productRepository.add(product)

        val deleteResult = productRepository.delete(1)
        assertTrue(deleteResult, "Delete should succeed")

        val remainingProducts = productRepository.getAll()
        assertTrue(remainingProducts.isEmpty(), "All products should be deleted")
    }

    @Test
    fun `test deleteAll products`() = runBlocking {
        productRepository.add(
            Product(
                1,
                "Product 1",
                "Desc 1",
                BigDecimal.valueOf(15.0),
                "1234567890",
                BigDecimal.valueOf(15.0),
                BigDecimal.valueOf(15.0),
                0L,
                BigDecimal(1),
                0,
                1
            )
        )
        productRepository.add(
            Product(
                2,
                "Product 2",
                "Desc 2",
                BigDecimal.valueOf(15.0),
                "112345678",
                BigDecimal.valueOf(15.0),
                BigDecimal.valueOf(15.0),
                0L,
                BigDecimal(1),
                0,
                1
            )
        )

        assertEquals(2, productRepository.getAll().size)
        assertTrue(productRepository.deleteAll(), "All products should be deleted")
        assertTrue(productRepository.getAll().isEmpty(), "Product list should be empty")
    }
}
