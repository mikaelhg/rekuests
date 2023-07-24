package rekuests.tests

import io.javalin.Javalin
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class PostTests {

    private lateinit var engine: Javalin

    private val localPort = 25812
    private val baseUrl = "http://127.0.0.1:${localPort}"

    @BeforeEach
    fun beforeEach() {
        engine = Javalin.create()
            .post("/data/12345") { ctx ->
                if (ctx.contentType() == "application/octet-stream"
                    && ctx.bodyAsBytes().contentEquals(byteArrayOf(1, 2, 3, 4, 5)))
                {
                    ctx.status(200)
                } else {
                    ctx.status(500)
                }
            }
            .start(localPort)
    }

    @AfterEach
    fun afterEach() {
        engine.stop()
    }

    @Test
    fun withData() {
        val input = byteArrayOf(1, 2, 3, 4, 5)
        val r = rekuests.post("$baseUrl/data/12345") {
            headers["content-type"] = "application/octet-stream"
            data = input
        }
        Assertions.assertEquals(200, r.statusCode)
    }

}
