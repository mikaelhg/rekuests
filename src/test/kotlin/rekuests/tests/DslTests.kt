package rekuests.tests

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockserver.integration.ClientAndServer
import org.mockserver.junit.jupiter.MockServerExtension
import org.mockserver.junit.jupiter.MockServerSettings
import org.mockserver.model.HttpRequest.request
import org.mockserver.model.HttpResponse.response
import rekuests.Auth
import java.net.HttpCookie

@ExtendWith(MockServerExtension::class)
@MockServerSettings(ports = [8787, 8888])
class DslTests(val mockServer: ClientAndServer) {

    init {
        mockServer
                .`when`(
                        request()
                                .withMethod("GET")
                                .withPath("/cookies")
                                .withCookie("from-my", "browser")
                )
                .respond(
                        response()
                                .withStatusCode(200)
                                .withCookie("foo", "bar")
                                .withBody("""{"cookie": "OK"}""")
                )
        mockServer
                .`when`(
                        request()
                                .withMethod("GET")
                                .withPath("/cookies")
                                .withCookie("foo", "bar")
                )
                .respond(
                        response()
                                .withStatusCode(200)
                                .withCookie("foo", "bar")
                                .withBody("""{"foo": "bar"}""")
                )
        mockServer
                .`when`(
                        request()
                                .withMethod("GET")
                                .withPath("/cookies")
                )
                .respond(
                        response()
                                .withStatusCode(200)
                                .withBody("""{"no": "cookies"}""")
                )
    }

    @Test
    fun testDsl() {
        val url = "http://127.0.0.1:${mockServer.localPort}/cookies"
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
        val baseUrl = "http://127.0.0.1:${mockServer.localPort}"
        println("baseUrl: $baseUrl")

        val s = rekuests.Session()
        s.auth = Auth("asd", "asf")
        s.auth = "user" to "pass"
        s.headers["foo"] = "bar"
        s.headers.update("x-test", "true")
        s.headers.update("x-test" to "true")

        var r = s.get("$baseUrl/cookies") {
            // cookies("from-my" to "browser")
            cookies(HttpCookie("from-my", "browser").apply {
                domain = "127.0.0.1"
                secure = false
            })
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