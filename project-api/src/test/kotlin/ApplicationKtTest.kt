import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import mx.unam.fciencias.ids.eq1.module
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ApplicationKtTest {

  @Test
  fun testRoot() = testApplication {

   application {
    module()
   }
   val response = client.get("/")
   assertEquals(HttpStatusCode.OK, response.status)
   assertEquals("Hello, Working!", response.bodyAsText())
  }
}