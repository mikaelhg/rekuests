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
        .post("/data/abcdef") { ctx ->
            println(ctx.formParamMap())
            if (ctx.contentType() == "application/x-www-form-urlencoded"
                && ctx.formParam("a") == "b"
                && ctx.formParam("c") == "d"
                && ctx.formParam("e") == "f")
            {
                ctx.status(200).json(ctx.formParamMap())
            } else {
                ctx.status(500).json(ctx.formParamMap())
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

    @Test
    fun withFormData() {
        val r = rekuests.post("$baseUrl/data/abcdef") {
            headers["content-type"] = "application/x-www-form-urlencoded"
            data("a" to "b", "c" to "d", "e" to "f")
        }
        Assertions.assertEquals(200, r.statusCode)
    }

}
