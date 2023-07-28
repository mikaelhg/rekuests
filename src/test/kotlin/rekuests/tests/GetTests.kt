package rekuests.tests

import io.javalin.Javalin
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import rekuests.auth.BasicAuth

class GetTests : JavalinBase(25812) {

    override fun createEngine(): Javalin = Javalin.create()
        .get("/cookies") { ctx ->
            ctx.json(mapOf("cookies" to ctx.cookieMap()))
        }
        .get("/headers/link") { ctx ->
            ctx.status(200)
                .header("link", """<https://one.example.com>; rel="preconnect", <https://two.example.com>; rel="preconnect", <https://three.example.com>; rel="preconnect"""")
                .result("OK")
        }
        .get("/redirect") { ctx ->
            ctx.redirect("/redirect/result")
        }
        .get("/params") { ctx ->
            println(ctx.queryString())
            println(ctx.queryParamMap())
            if (ctx.queryParam("a") == "b"
                && ctx.queryParam("c") == "d"
                && ctx.queryParam("e") == "f")
            {
                ctx.status(200)
            } else {
                ctx.status(500)
            }
        }

    @Test
    fun simpleGet() {
        val r = rekuests.get("${baseUrl}/cookies")
        Assertions.assertEquals(200, r.statusCode)
    }

    @Test
    fun sessionTest() {
        val s = rekuests.Session().apply {
            auth = BasicAuth("asd", "asf")
            auth = "user" to "pass"
            headers["foo"] = "bar"
            headers.update("x-test", "true")
            headers.update("x-test" to "false")
        }
        var r = s.get("$baseUrl/cookies") {
            cookie("from-my", "browser")
        }
        Assertions.assertEquals("""{"cookies":{"from-my":"browser"}}""", r.text)

        r = rekuests.get("$baseUrl/cookies")
        Assertions.assertEquals("""{"cookies":{}}""", r.text)
        // val json = r.json()["cookies"]?.get("from-my")?.getContent()
    }

    @Test
    fun withLinkHeader() {
        val r = rekuests.get("$baseUrl/headers/link")
        Assertions.assertEquals("https://one.example.com", r.links[0]["url"])
        Assertions.assertEquals("https://two.example.com", r.links[1]["url"])
        Assertions.assertEquals("https://three.example.com", r.links[2]["url"])
        Assertions.assertEquals("preconnect", r.links[0]["rel"])
        Assertions.assertEquals("preconnect", r.links[1]["rel"])
        Assertions.assertEquals("preconnect", r.links[2]["rel"])
    }

    @Test
    fun redirect() {
        val r = rekuests.get("$baseUrl/redirect") {
            followRedirects = false
        }
        Assertions.assertTrue(r.isRedirect)
        Assertions.assertEquals(302, r.statusCode)
    }

    @Test
    fun parameters() {
        val r = rekuests.get("$baseUrl/params") {
            params("a" to "b", "c" to "d", "e" to "f")
        }
        Assertions.assertEquals(200, r.statusCode)
    }

}
