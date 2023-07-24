package rekuests

import rekuests.util.Headers
import java.io.Closeable
import java.net.CookieManager
import java.net.CookiePolicy

open class Session : AutoCloseable, Closeable {

    var auth: Auth? = null

    var headers = Headers()

    val cookieManager = newCookieManager()

    fun rekuest(method: String, url: String, init: Request.() -> Unit = {}): Response =
        Request(method, url, this)
            .apply(init)
            .mergeSession(this)
            .execute()

    fun get(url: String, init: Request.() -> Unit = {}) = rekuest("GET", url, init)

    fun post(url: String, init: Request.() -> Unit) = rekuest("POST", url, init)

    fun put(url: String, init: Request.() -> Unit) = rekuest("PUT", url, init)

    fun patch(url: String, init: Request.() -> Unit) = rekuest("PATCH", url, init)

    fun delete(url: String, init: Request.() -> Unit) = rekuest("DELETE", url, init)

    fun head(url: String, init: Request.() -> Unit) = rekuest("HEAD", url, init)

    fun options(url: String, init: Request.() -> Unit) = rekuest("OPTIONS", url, init)

    override fun close() {
        // kill any keepalive sockets
    }

    protected open fun newCookieManager() = CookieManager(null, CookiePolicy.ACCEPT_ALL)

}

typealias Auth = Pair<String, String>
