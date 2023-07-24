package rekuests.tests

import io.javalin.Javalin
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class PostTests : JavalinBase(25813) {

    override fun createEngine(): Javalin = Javalin.create()
        .post("/data/12345") { ctx ->
            if (ctx.contentType() == "application/octet-stream"
                && ctx.bodyAsBytes().contentEquals(byteArrayOf(1, 2, 3, 4, 5)))
            {
                ctx.status(200)
            } else {
                ctx.status(500)
            }
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
