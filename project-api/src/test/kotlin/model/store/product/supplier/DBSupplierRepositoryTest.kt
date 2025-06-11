package model.store.product.supplier

import kotlinx.coroutines.runBlocking
import mx.unam.fciencias.ids.eq1.model.store.CreateStoreRequest
import mx.unam.fciencias.ids.eq1.model.store.product.Product
import mx.unam.fciencias.ids.eq1.model.store.product.repository.DBProductRepository
import mx.unam.fciencias.ids.eq1.model.store.product.supplier.CreateSupplierRequest
import mx.unam.fciencias.ids.eq1.model.store.product.supplier.UpdateSupplierRequest
import mx.unam.fciencias.ids.eq1.model.store.product.supplier.repository.DBSupplierRepository
import mx.unam.fciencias.ids.eq1.model.store.repository.DBStoreRepository
import mx.unam.fciencias.ids.eq1.model.user.User
import mx.unam.fciencias.ids.eq1.model.user.repository.DBUserRepository
import mx.unam.fciencias.ids.eq1.model.user.repository.UserRepository
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import java.time.Instant
import java.util.*
import kotlin.test.*

class DBSupplierRepositoryTest {

    private lateinit var database: Database
    private lateinit var supplierRepository: DBSupplierRepository
    private lateinit var productRepository: DBProductRepository
    private lateinit var storeRepository: DBStoreRepository
    private lateinit var userRepository: UserRepository
    private val storeId = 1
    val dbName = "test-${UUID.randomUUID()}"

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
            url = "jdbc:h2:mem:$dbName;DB_CLOSE_DELAY=-1",
            driver = "org.h2.Driver"
        )

        userRepository = DBUserRepository(database)
        storeRepository = DBStoreRepository(database)
        productRepository = DBProductRepository(database, storeId)
        supplierRepository = DBSupplierRepository(database, storeId)

        runBlocking {
            users.forEach { userRepository.add(it) }
            stores.forEachIndexed { index, store -> storeRepository.add(store, users[index]) }
        }

        startKoin {
            modules(
                module {
                    single { supplierRepository }
                }
            )
        }
    }

    @AfterEach
    fun tearDown() {
        transaction(database) {
            exec("DROP ALL OBJECTS") // H2 Specific Syntax
        }
        stopKoin()
    }

    @Test
    fun `test add and retrieve supplier`() = runBlocking {
        val supplierRequest = CreateSupplierRequest(
            name = "Sample Supplier",
            contactName = "John Doe",
            contactPhone = "123-456-7890",
            email = "john@supplier.com",
            address = "123 Supplier St."
        )

        val result = supplierRepository.add(supplierRequest)
        assertTrue(result != -1, "Supplier should be added successfully")

        val allSuppliers = supplierRepository.getAll()
        assertEquals(1, allSuppliers.size)

        val savedSupplier = allSuppliers[0]
        assertEquals("Sample Supplier", savedSupplier.name)
        assertEquals("John Doe", savedSupplier.contactName)
        assertEquals("123-456-7890", savedSupplier.contactPhone)
    }

    @Test
    fun `test getById`() = runBlocking {
        val supplierRequest = CreateSupplierRequest(
            name = "Unique Supplier",
            contactName = "Jane Smith",
            contactPhone = "987-654-3210",
            email = "jane@supplier.com",
            address = "456 Supplier Ave."
        )

        val supplierId = supplierRepository.add(supplierRequest)

        val foundSupplier = supplierRepository.getById(supplierId)
        assertNotNull(foundSupplier)
        assertEquals("Unique Supplier", foundSupplier.name)

        val nonExistentSupplier = supplierRepository.getById(999)
        assertNull(nonExistentSupplier)
    }

    @Test
    fun `test update supplier`() = runBlocking {
        val supplierRequest = CreateSupplierRequest(
            name = "Old Name",
            contactName = "Old Contact",
            contactPhone = "111-222-3333",
            email = "old@email.com",
            address = "Old Address"
        )

        val supplierId = supplierRepository.add(supplierRequest)

        val updateRequest = UpdateSupplierRequest(
            id = supplierId,
            name = "Updated Name",
            contactName = "Updated Contact",
            contactPhone = "444-555-6666",
            email = "updated@email.com",
            address = "Updated Address"
        )

        val updateResult = supplierRepository.update(supplierId, updateRequest)
        assertTrue(updateResult, "Update should succeed")

        val retrievedSupplier = supplierRepository.getById(supplierId)
        assertNotNull(retrievedSupplier)
        assertEquals("Updated Name", retrievedSupplier.name)
        assertEquals("Updated Contact", retrievedSupplier.contactName)
        assertEquals("444-555-6666", retrievedSupplier.contactPhone)
        assertEquals("updated@email.com", retrievedSupplier.email)
        assertEquals("Updated Address", retrievedSupplier.address)
    }

    @Test
    fun `test partial update supplier`() = runBlocking {
        val supplierRequest = CreateSupplierRequest(
            name = "Original Name",
            contactName = "Original Contact",
            contactPhone = "111-222-3333",
            email = "original@email.com",
            address = "Original Address"
        )

        val supplierId = supplierRepository.add(supplierRequest)

        // Only update name and phone
        val partialUpdateRequest = UpdateSupplierRequest(
            id = supplierId,
            name = "New Name",
            contactName = null,
            contactPhone = "999-888-7777",
            email = null,
            address = null
        )

        val updateResult = supplierRepository.update(supplierId, partialUpdateRequest)
        assertTrue(updateResult, "Partial update should succeed")

        val retrievedSupplier = supplierRepository.getById(supplierId)
        assertNotNull(retrievedSupplier)
        assertEquals("New Name", retrievedSupplier.name)
        assertEquals("Original Contact", retrievedSupplier.contactName)  // Should remain unchanged
        assertEquals("999-888-7777", retrievedSupplier.contactPhone)
        assertEquals("original@email.com", retrievedSupplier.email)  // Should remain unchanged
        assertEquals("Original Address", retrievedSupplier.address)  // Should remain unchanged
    }

    @Test
    fun `test delete supplier`() = runBlocking {
        val supplierRequest = CreateSupplierRequest(
            name = "To Delete",
            contactName = "Delete Contact",
            contactPhone = "123-456-7890",
            email = "delete@email.com",
            address = "Delete Address"
        )

        val supplierId = supplierRepository.add(supplierRequest)

        val deleteResult = supplierRepository.delete(supplierId)
        assertTrue(deleteResult, "Delete should succeed")

        val deletedSupplier = supplierRepository.getById(supplierId)
        assertNull(deletedSupplier, "Supplier should be deleted")
    }

    @Test
    fun `test getByName`() = runBlocking {
        // Add multiple suppliers
        val supplier1 = CreateSupplierRequest(
            name = "ABC Company",
            contactName = "Contact 1",
            contactPhone = "111-222-3333",
            email = "abc@email.com",
            address = "ABC Address"
        )

        val supplier2 = CreateSupplierRequest(
            name = "XYZ Company",
            contactName = "Contact 2",
            contactPhone = "444-555-6666",
            email = "xyz@email.com",
            address = "XYZ Address"
        )

        val supplier3 = CreateSupplierRequest(
            name = "ABC Distribution",
            contactName = "Contact 3",
            contactPhone = "777-888-9999",
            email = "abcDist@email.com",
            address = "Distribution Address"
        )

        supplierRepository.add(supplier1)
        supplierRepository.add(supplier2)
        supplierRepository.add(supplier3)

        // Test partial name match
        val abcSuppliers = supplierRepository.getByName("ABC")
        assertEquals(2, abcSuppliers.size)
        assertTrue(abcSuppliers.any { it.name == "ABC Company" })
        assertTrue(abcSuppliers.any { it.name == "ABC Distribution" })

        // Test exact name match
        val xyzSuppliers = supplierRepository.getByName("XYZ Company")
        assertEquals(1, xyzSuppliers.size)
        assertEquals("XYZ Company", xyzSuppliers[0].name)

        // Test no match
        val noMatchSuppliers = supplierRepository.getByName("NonExistent")
        assertTrue(noMatchSuppliers.isEmpty())
    }

    @Test
    fun `test getByContact`() = runBlocking {
        // Add suppliers with different contacts
        val supplier1 = CreateSupplierRequest(
            name = "Supplier 1",
            contactName = "John Smith",
            contactPhone = "111-222-3333",
            email = "john@email.com",
            address = "Address 1"
        )

        val supplier2 = CreateSupplierRequest(
            name = "Supplier 2",
            contactName = "Jane Smith",
            contactPhone = "444-555-6666",
            email = "jane@email.com",
            address = "Address 2"
        )

        supplierRepository.add(supplier1)
        supplierRepository.add(supplier2)

        val smithSuppliers = supplierRepository.getByContact("Smith")
        assertEquals(2, smithSuppliers.size)

        val johnSuppliers = supplierRepository.getByContact("John")
        assertEquals(1, johnSuppliers.size)
        assertEquals("John Smith", johnSuppliers[0].contactName)

        val noMatchSuppliers = supplierRepository.getByContact("Williams")
        assertTrue(noMatchSuppliers.isEmpty())
    }

    @Test
    fun `test existsById`() = runBlocking {
        val supplierRequest = CreateSupplierRequest(
            name = "Existing Supplier",
            contactName = "Contact Person",
            contactPhone = "123-456-7890",
            email = "exist@email.com",
            address = "Existing Address"
        )

        val supplierId = supplierRepository.add(supplierRequest)

        assertTrue(supplierRepository.existsById(supplierId))
        assertFalse(supplierRepository.existsById(999))
    }


    @Test
    fun `test product-supplier linking`() = runBlocking {
        val product = Product(
            id = 999,
            name = "Product Name",
            description = "Description",
            price = 20.0.toBigDecimal(),
            wholesalePrice = 3.14.toBigDecimal(),
            retailPrice = 3.14.toBigDecimal(),
            stock = 10.toBigDecimal(),
            minAllowStock = 1,
            barcode = "123",
            storeId = storeId,
            createdAt = System.currentTimeMillis()
        )

        val add = productRepository.add(product)


        val supplierId = supplierRepository.add(CreateSupplierRequest("SupplierX", "XContact", "321", "x@email.com", "X St"))
        assertTrue(supplierRepository.addProductSupply(supplierId, 1))

        val products = supplierRepository.getAllProductsSupplier(supplierId)
        assertEquals(1, products.size)
        assertEquals("Product Name", products[0].name)

        assertTrue(supplierRepository.suppliesProducts(supplierId, 1))

        assertTrue(supplierRepository.removeProductSupply(supplierId, add))
        assertFalse(supplierRepository.suppliesProducts(supplierId, 1))
    }
}