package rekuests.tests

import org.junit.jupiter.api.Test

class DslTests {

    @Test
    fun testDsl() {
        val r = rekuests.get("https://www.google.com/") {
            auth("user", "pass")
            params("a" to "b")
        }
        r.headers["content-type"]
        r.encoding
        r.text
        r.json()

    }

}