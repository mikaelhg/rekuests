package rekuests.tests

import io.javalin.Javalin
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import rekuests.Auth

class DslTests {

    private lateinit var engine: Javalin

    private val localPort = 25812

    @BeforeEach
    fun beforeEach() {
        engine = Javalin.create()
            .get("/cookies") { ctx ->
                ctx.json(mapOf("cookies" to ctx.cookieMap()))
            }
            .start(localPort)
    }

    @AfterEach
    fun afterEach() {
        engine.stop()
    }

    @Test
    fun testDsl() {
        val url = "http://127.0.0.1:${localPort}/cookies"
        val r = rekuests.get(url) {
            auth("user", "pass")
            params("a" to "b")
        }
        r.headers["content-type"]
        r.encoding
        r.text
        r.json()
    }

    @Test
    fun sessionTest() {
        val baseUrl = "http://127.0.0.1:${localPort}"
        println("baseUrl: $baseUrl")

        val s = rekuests.Session()
        s.auth = Auth("asd", "asf")
        s.auth = "user" to "pass"
        s.headers["foo"] = "bar"
        s.headers.update("x-test", "true")
        s.headers.update("x-test" to "true")

        var r = s.get("$baseUrl/cookies") {
            cookie("from-my", "browser")
        }
        println(r.text)
        // '{"cookies": {"from-my": "browser"}}'

        r = s.get("$baseUrl/cookies")
        println(r.text)
        // '{"cookies": {}}'

        /*
        rekuests.Session().use { s ->
            s.get("https://httpbin.org/cookies/set/sessioncookie/123456789")
        }
        */

    }

}