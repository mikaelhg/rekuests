package rekuests.tests

import io.javalin.Javalin
import kotlinx.serialization.json.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import rekuests.Auth

class DslTests {

    private lateinit var engine: Javalin

    private val localPort = 25812
    private val baseUrl = "http://127.0.0.1:${localPort}"

    @BeforeEach
    fun beforeEach() {
        engine = Javalin.create()
            .get("/cookies") { ctx ->
                ctx.json(mapOf("cookies" to ctx.cookieMap()))
            }
            .get("/headers/link") { ctx ->
                ctx.status(200)
                    .header("link", """<https://one.example.com>; rel="preconnect", <https://two.example.com>; rel="preconnect", <https://three.example.com>; rel="preconnect"""")
                    .result("OK")
            }
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
    fun testDsl() {
        val url = "${baseUrl}/cookies"
        val r = rekuests.get(url) {
            auth("user", "pass")
            params("a" to "b")
        }
    }

    @Test
    fun sessionTest() {
        val s = rekuests.Session().apply {
            auth = Auth("asd", "asf")
            auth = "user" to "pass"
            headers["foo"] = "bar"
            headers.update("x-test", "true")
            headers.update("x-test" to "true")
        }
        var r = s.get("$baseUrl/cookies") {
            cookie("from-my", "browser")
        }
        Assertions.assertEquals("""{"cookies":{"from-my":"browser"}}""", r.text)
        r = rekuests.get("$baseUrl/cookies")
        Assertions.assertEquals("""{"cookies":{}}""", r.text)
        // val json = r.json()["cookies"]?.get("from-my")?.getContent()
    }

    operator fun JsonElement.get(key: String): JsonElement? {
        return (this as? JsonObject)?.get(key)
    }

    fun JsonElement.getContent(): String? {
        return (this as? JsonPrimitive)?.content
    }

    @Test
    fun withData() {
        val r = rekuests.post("$baseUrl/data/12345") {
            headers["content-type"] = "application/octet-stream"
            data = byteArrayOf(1, 2, 3, 4, 5)
        }
        Assertions.assertEquals(200, r.statusCode)
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

}