package model.store.repository

import kotlinx.coroutines.runBlocking
import mx.unam.fciencias.ids.eq1.db.store.StoreTable
import mx.unam.fciencias.ids.eq1.db.user.UserTable
import mx.unam.fciencias.ids.eq1.model.store.CreateStoreRequest
import mx.unam.fciencias.ids.eq1.model.store.UpdateStoreRequest
import mx.unam.fciencias.ids.eq1.model.store.repository.DBStoreRepository
import mx.unam.fciencias.ids.eq1.model.user.User
import mx.unam.fciencias.ids.eq1.model.user.repository.DBUserRepository
import mx.unam.fciencias.ids.eq1.model.user.repository.UserRepository
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Assert.assertFalse
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import java.time.Instant
import kotlin.test.*

class DBStoreRepositoryTest : KoinTest {

    private lateinit var database: Database
    private lateinit var storeRepository: DBStoreRepository
    private lateinit var userRepository: UserRepository
    private lateinit var testOwner: User

    @BeforeEach
    fun setUp() {
        stopKoin()

        database = Database.connect(
            url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
            driver = "org.h2.Driver"
        )

        transaction(database) {
            SchemaUtils.create(StoreTable)
        }

        userRepository = DBUserRepository(database)
        storeRepository = DBStoreRepository(database)


        // Create a test user to be the owner of stores
        runBlocking {
            val testUser = User(
                name = "Store Owner",
                email = "owner@example.com",
                hashedPassword = "hashedpass",
                salt = "testsalt",
                createdAt = Instant.now().epochSecond
            )
            userRepository.add(testUser)
            testOwner = userRepository.getByName("Store Owner")!!
        }

    }

    @AfterEach
    fun tearDown() {
        transaction(database) {
            SchemaUtils.drop(StoreTable)
            SchemaUtils.drop(UserTable)
        }
        stopKoin()
    }

    @Test
    fun testGetStoreById() {
        runBlocking {
            val testStore = CreateStoreRequest(
                name = "Test Store",
                address = "123 Test St"
            )


            storeRepository.add(testStore, testOwner)
            val stores = storeRepository.getAll()
            val storeId = stores.first().id

            val retrievedStore = storeRepository.getById(storeId)
            assertNotNull(retrievedStore, "Should find store by ID")
            assertEquals("Test Store", retrievedStore.name)
            assertEquals(testOwner.id, retrievedStore.owner)
        }
    }

    @Test
    fun testGetStoreByNonExistentId() {
        runBlocking {
            val nonExistentStore = storeRepository.getById(9999)
            assertNull(nonExistentStore, "Should return null for non-existent store ID")
        }
    }

    @Test
    fun testGetStoresByOwnerId() {
        runBlocking {
            val store1 = CreateStoreRequest( "Store 1", "Address 1")
            val store2 = CreateStoreRequest("Store 2",  "Address 2")

            storeRepository.add(store1, testOwner)
            storeRepository.add(store2, testOwner)

            val ownerStores = storeRepository.getByOwnerId(testOwner.id)
            assertEquals(2, ownerStores.size, "Should find 2 stores for the owner")

            val storeNames = ownerStores.map { it.name }.toSet()
            assertTrue(storeNames.contains("Store 1"), "Should contain first store")
            assertTrue(storeNames.contains("Store 2"), "Should contain second store")
        }
    }

    @Test
    fun testGetStoresByNonExistentOwnerId() {
        runBlocking {
            val nonExistentOwnerStores = storeRepository.getByOwnerId(9999)
            assertTrue(nonExistentOwnerStores.isEmpty(), "Should return empty list for non-existent owner")
        }
    }

    @Test
    fun testGetAllStores() {
        runBlocking {
            val secondUser = User(
                name = "Second Owner",
                email = "second@example.com",
                hashedPassword = "hashedpass",
                salt = "testsalt",
                createdAt = Instant.now().epochSecond
            )
            userRepository.add(secondUser)
            val secondOwnerId = userRepository.getByName("Second Owner")!!

            val store1 = CreateStoreRequest( "Store 1", "Address 1")
            val store2 = CreateStoreRequest("Store 2",  "Address 2")
            storeRepository.add(store1, testOwner)
            storeRepository.add(store2, secondOwnerId)

            val allStores = storeRepository.getAll()
            assertEquals(2, allStores.size, "Should find all stores regardless of owner")

            val storeNames = allStores.map { it.name }.toSet()
            assertTrue(storeNames.contains("Store 1"), "Should contain first owner's store")
            assertTrue(storeNames.contains("Store 2"), "Should contain second owner's store")
        }
    }

    @Test
    fun testGetAllStoresWithEmptyDatabase() {
        runBlocking {
            val emptyStoreList = storeRepository.getAll()
            assertTrue(emptyStoreList.isEmpty(), "Should return empty list when no stores exist")
        }
    }


    @Test
    fun testAddStore() {
        runBlocking {

            val newStore = CreateStoreRequest(
                name = "New Store",
                address = "456 New St",
            )

            val result = storeRepository.add(newStore, testOwner)
            assertTrue("Store should be added successfully") {
                return@assertTrue result != -1
            }

            val allStores = storeRepository.getAll()
            assertEquals(1, allStores.size, "Should have 1 store")

            val savedStore = allStores[0]
            assertEquals("New Store", savedStore.name)
            assertEquals("456 New St", savedStore.address)
            assertEquals(testOwner.id, savedStore.owner)
        }
    }


    @Test
    fun testAddStoreWithInvalidData() {
        runBlocking {
            val invalidStore = CreateStoreRequest(
                name = "Invalid Store",
                address = "Invalid Address",
            )

            val result = storeRepository.add(invalidStore, testOwner.copy(id = -1))
            assertTrue("Should fail to add store with invalid owner ID") { result == -1 }

            val store1 = CreateStoreRequest( "Duplicate Name", "First store")
            val store2 = CreateStoreRequest("Duplicate Name", "Second store")

            storeRepository.add(store1, testOwner)
            val duplicateResult = storeRepository.add(store2, testOwner)
            assertTrue( "Should fail to add store with duplicate name for same owner") {
                    duplicateResult == -1
            }
        }
    }

    @Test
    fun testDeleteStore() {
        runBlocking {
            val storeToDelete = CreateStoreRequest( "To Delete", "Will be deleted")
            val storeToKeep = CreateStoreRequest( "To Keep", "Will be kept")

            storeRepository.add(storeToDelete, testOwner)
            storeRepository.add(storeToKeep, testOwner)

            val allStores = storeRepository.getAll()
            assertEquals(2, allStores.size, "Should have 2 stores initially")

            val storeIdToDelete = allStores.first { it.name == "To Delete" }.id

            val deleteResult = storeRepository.delete(storeIdToDelete)
            assertTrue(deleteResult, "Delete should succeed")

            val remainingStores = storeRepository.getAll()
            assertEquals(1, remainingStores.size, "Should have 1 store after deletion")
            assertEquals("To Keep", remainingStores.first().name, "Correct store should remain")
        }
    }


    @Test
    fun testDeleteNonExistentStore() {
        runBlocking {
            val failedDelete = storeRepository.delete(9999)
            assertFalse("Should return false when deleting non-existent store", failedDelete)
        }
    }


    @Test
    fun testDeleteStoreWithRelatedEntities() {
        runBlocking {
            val storeWithProducts = CreateStoreRequest( "Store With Products", "Has products")
            storeRepository.add(storeWithProducts, testOwner)

            val storeId = storeRepository.getAll().first().id

            // productRepository.add(new Product(storeId, ...))

            val deleteResult = storeRepository.delete(storeId)
            assertTrue(deleteResult, "Delete should succeed even with related entities")

            val storeAfterDelete = storeRepository.getById(storeId)
            assertNull(storeAfterDelete, "Store should be deleted")

            // val productsAfterDelete = productRepository.getByStoreId(storeId)
            // assertTrue(productsAfterDelete.isEmpty(), "Related products should be deleted")
        }
    }

    @Test
    fun testUpdateStore() {
        runBlocking {
            val originalStore = CreateStoreRequest( "Original Name", "Original description")
            storeRepository.add(originalStore, testOwner)

            val storeId = storeRepository.getAll().first().id

            val updatedStore = UpdateStoreRequest(
                newName = "Updated Name",
                newAddress = "Updated address"
            )

            val updateResult = storeRepository.update(storeId ,updatedStore)
            assertTrue("Update should succeed") {updateResult}

            val retrievedStore = storeRepository.getById(storeId)
            assertNotNull(retrievedStore, "Should be able to retrieve updated store")
            assertEquals("Updated Name", retrievedStore.name, "Name should be updated")
            assertEquals("Updated address", retrievedStore.address, "Address should be updated")
        }
    }

    @Test
    fun testUpdateNonExistentStore() {
        runBlocking {
            val nonExistentStore = UpdateStoreRequest(
                newName = "Non-existent Store",
                newAddress = "Nowhere",
            )

            val updateResult = storeRepository.update(1 ,nonExistentStore)
            assertFalse( "Should return false when updating non-existent store") { updateResult }
        }
    }
}