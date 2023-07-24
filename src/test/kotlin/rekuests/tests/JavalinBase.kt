package rekuests.tests

import io.javalin.Javalin
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

abstract class JavalinBase(private var localPort: Int) {

    protected lateinit var engine: Javalin

    protected val baseUrl = "http://127.0.0.1:${localPort}"

    abstract fun createEngine(): Javalin

    @BeforeEach
    fun beforeEach() {
        engine = createEngine().start(localPort)
    }

    @AfterEach
    fun afterEach() {
        engine.stop()
    }

}
