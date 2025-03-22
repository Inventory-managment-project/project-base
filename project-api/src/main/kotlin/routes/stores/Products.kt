package mx.unam.fciencias.ids.eq1.routes.stores

import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mx.unam.fciencias.ids.eq1.model.store.product.Product
import mx.unam.fciencias.ids.eq1.routes.getStoreIdOrBadRequest
import mx.unam.fciencias.ids.eq1.routes.verifyUserIsOwner
import mx.unam.fciencias.ids.eq1.service.store.product.ProductService
import org.koin.core.parameter.parametersOf
import org.koin.ktor.ext.inject




private suspend fun RoutingCall.getProductIdOrBadRequest(): Int? {
    val storeId = this.parameters["productId"]?.toIntOrNull()
    if (storeId == null) this.respond(HttpStatusCode.NoContent, "Product not found")
    return storeId
}

private suspend fun RoutingCall.getProductBarcodeOrBadRequest(): String? {
    val storeId = this.parameters["productId"]
    if (storeId == null) this.respond(HttpStatusCode.NoContent, "Product not found")
    return storeId
}

/**
 * Defines routes for managing products within stores.
 *
 * **Authentication:** Requires JWT authentication with the "auth-jwt" scheme.
 *
 * **Endpoints:**
 *
 * **Get All Products**
 * - URL: `/{storeId}/products`
 * - Method: `GET`
 * - Response:
 *     - 200 OK: List of all [Product].
 *     - 404 Not Found: If the user is not authorized or the store is missing.
 *
 * **Add Multiple Products**
 * - URL: `/{storeId}/products`
 * - Method: `POST`
 * - Request Body: List of [Product] objects. ID, CreatedAt will be ignored and reassign when adding to store inventory
 * - Response:
 *     - 201 Created: List of newly created product IDs.
 *     - 404 Not Found: If the user is not authorized or the store is missing.
 *
 * **Get Single Product by ID**
 * - URL: `/{storeId}/product/id/{productId}`
 * - Method: `GET`
 * - Response:
 *     - 200 OK: JSON [Product].
 *     - 404 Not Found: If the product is missing or the user is not authorized.
 *
 * **Delete Product by ID**
 * - URL: `/{storeId}/product/id/{productId}`
 * - Method: `DELETE`
 * - Response:
 *     - 200 OK: Successful deletion.
 *     - 404 Not Found: If the product is missing or the user is not authorized.
 *
 * **Get Single Product by ID**
 * - URL: `/{storeId}/product/barcode/{productBarcode}`
 * - Method: `GET`
 * - Response:
 *     - 200 OK: JSON [Product].
 *     - 404 Not Found: If the product is missing or the user is not authorized.
 *
 * **Delete Product by ID**
 * - URL: `/{storeId}/product/barcode/{productBarcode}`
 * - Method: `DELETE`
 * - Response:
 *     - 200 OK: Successful deletion.
 *     - 404 Not Found: If the product is missing or the user is not authorized.
 *
 * **Add a Single Product**
 * - URL: `/{storeId}/product`
 * - Method: `POST`
 * - Request Body: A [Product] object.
 * - Response:
 *     - 201 Created: Returns the newly created product ID.
 *     - 404 Not Found: If the user is not authorized.
 *
 * **Update an Existing Product**
 * - URL: `/{storeId}/product`
 * - Method: `PUT`
 * - Request Body: A [Product] object with updated details. Its Updated Based on ID.
 * - Response:
 *     - 200 OK: Successfully updated product.
 *     - 409 Conflict: If the update operation fails.
 */
fun Route.products() {
    authenticate("auth-jwt") {
        route("/{storeId}/products") {
            get {
                val storeId = call.getStoreIdOrBadRequest() ?: return@get
                if (!call.verifyUserIsOwner()) return@get call.respond(HttpStatusCode.NotFound)
                val productService by call.inject<ProductService> { parametersOf(storeId) }
                call.respond(productService.getAllProducts())
            }
            post {
                val storeId = call.getStoreIdOrBadRequest() ?: return@post
                if (!call.verifyUserIsOwner()) return@post call.respond(HttpStatusCode.NotFound)
                val products = call.receive<List<Product>>()
                val productService by call.inject<ProductService> { parametersOf(storeId) }
                val productsId = products.map { productService.addProduct(it) }
                call.respond(HttpStatusCode.Created, productsId)
            }
        }

        route("/{storeId}/product") {
            route("/id/{productId}") {
                get {
                    if (!call.verifyUserIsOwner()) return@get call.respond(HttpStatusCode.NotFound)
                    val storeId = call.getStoreIdOrBadRequest() ?: return@get
                    val productId = call.getProductIdOrBadRequest() ?: return@get
                    val productService by call.inject<ProductService> { parametersOf(storeId) }
                    val product =
                        productService.getProductById(productId) ?: return@get call.respond(HttpStatusCode.NotFound)
                    call.respond(HttpStatusCode.OK, product)
                }
                delete {
                    if (!call.verifyUserIsOwner()) return@delete call.respond(HttpStatusCode.NotFound)
                    val storeId = call.getStoreIdOrBadRequest() ?: return@delete
                    val productId = call.getProductIdOrBadRequest() ?: return@delete
                    val productService by call.inject<ProductService> { parametersOf(storeId) }
                    if (productService.deleteProductById(productId)) {
                        call.respond(HttpStatusCode.OK)
                    } else {
                        call.respond(HttpStatusCode.NotFound)
                    }
                }
            }
            route("/barcode/{productId}") {
                get {
                    if (!call.verifyUserIsOwner()) return@get call.respond(HttpStatusCode.NotFound)
                    val storeId = call.getStoreIdOrBadRequest() ?: return@get
                    val productId = call.getProductBarcodeOrBadRequest() ?: return@get
                    val productService by call.inject<ProductService> { parametersOf(storeId) }
                    val product =
                        productService.getProductByBarcode(productId) ?: return@get call.respond(HttpStatusCode.NotFound)
                    call.respond(HttpStatusCode.OK, product)
                }
                delete {
                    if (!call.verifyUserIsOwner()) return@delete call.respond(HttpStatusCode.NotFound)
                    val storeId = call.getStoreIdOrBadRequest() ?: return@delete
                    val productId = call.getProductBarcodeOrBadRequest() ?: return@delete
                    val productService by call.inject<ProductService> { parametersOf(storeId) }
                    if (productService.deleteProductByBarcode(productId)) {
                        call.respond(HttpStatusCode.OK)
                    } else {
                        call.respond(HttpStatusCode.NotFound)
                    }
                }
            }
            post {
                if (!call.verifyUserIsOwner()) return@post call.respond(HttpStatusCode.NotFound)
                val storeId = call.getStoreIdOrBadRequest() ?: return@post
                val product = call.receive<Product>()
                val productService by call.inject<ProductService> { parametersOf(storeId) }
                val productId = productService.addProduct(product)
                call.respond(HttpStatusCode.Created, mapOf("id" to productId))
            }
            put {
                if (!call.verifyUserIsOwner()) return@put call.respond(HttpStatusCode.NotFound)
                val storeId = call.getStoreIdOrBadRequest() ?: return@put
                val updatedProduct = call.receive<Product>()
                val productService by call.inject<ProductService> { parametersOf(storeId) }
                if (productService.updateProduct(updatedProduct)) {
                    call.respond(HttpStatusCode.OK, updatedProduct)
                } else {
                    call.respond(HttpStatusCode.Conflict)
                }
            }
        }
    }
}