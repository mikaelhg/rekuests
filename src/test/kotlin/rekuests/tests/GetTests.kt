package rekuests.tests

import io.javalin.Javalin
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import rekuests.auth.BasicAuth

class GetTests : JavalinBase(25812) {

    override fun createEngine(): Javalin = Javalin.create()
        .get("/cookies") { ctx ->
            ctx.json(mapOf("cookies" to ctx.cookieMap()))
        }
        .get("/cookies/ab") { ctx ->
            if (ctx.cookie("a") == "b") {
                ctx.status(200).cookie("c", "d")
            } else {
                ctx.status(500)
            }
        }
        .get("/headers/link") { ctx ->
            ctx.status(200)
                .header("link", """<https://one.example.com>; rel="preconnect", <https://two.example.com>; rel="preconnect", <https://three.example.com>; rel="preconnect"""")
                .result("OK")
        }
        .get("/redirect") { ctx ->
            ctx.redirect("/redirect/result")
        }
        .get("/params/abcdef") { ctx ->
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
        assertEquals(200, r.statusCode)
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
        val r = s.get("$baseUrl/cookies") {
            cookie("from-my", "browser")
        }
        assertEquals("""{"cookies":{"from-my":"browser"}}""", r.text)
    }

    @Test
    fun withLinkHeader() {
        val r = rekuests.get("$baseUrl/headers/link")
        assertEquals("https://one.example.com", r.links[0]["url"])
        assertEquals("https://two.example.com", r.links[1]["url"])
        assertEquals("https://three.example.com", r.links[2]["url"])
        assertEquals("preconnect", r.links[0]["rel"])
        assertEquals("preconnect", r.links[1]["rel"])
        assertEquals("preconnect", r.links[2]["rel"])
    }

    @Test
    fun redirect() {
        val r = rekuests.get("$baseUrl/redirect") {
            followRedirects = false
        }
        assertTrue(r.isRedirect)
        assertEquals(302, r.statusCode)
    }

    @Test
    fun parameters() {
        val r = rekuests.get("$baseUrl/params/abcdef") {
            params("a" to "b", "c" to "d", "e" to "f")
        }
        assertEquals(200, r.statusCode)
    }

    @Test
    fun cookies() {
        val r = rekuests.get("$baseUrl/cookies/ab") {
            cookie("a", "b")
        }
        assertEquals(200, r.statusCode)
        assertEquals("c", r.cookies[1].name)
        assertEquals("d", r.cookies[1].value)
    }

}
