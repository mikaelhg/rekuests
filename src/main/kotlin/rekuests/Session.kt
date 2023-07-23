package rekuests

import rekuests.util.Headers
import java.io.Closeable
import java.net.CookieManager
import java.net.CookiePolicy

open class Session : AutoCloseable, Closeable {

    var auth: Auth? = null

    var headers = Headers()

    val cookieManager = newCookieManager()

    fun rekuest(method: String, url: String, init: Request.() -> Unit): Response =
        Request(method, url, this)
            .apply(init)
            .mergeSession(this)
            .execute()

    fun get(url: String): Response =
        Request("GET", url, this)
            .mergeSession(this)
            .execute()

    fun get(url: String, init: Request.() -> Unit): Response =
        Request("GET", url, this)
            .apply(init)
            .mergeSession(this)
            .execute()

    fun post(url: String, data: ByteArray, init: Request.() -> Unit): Response =
        Request("POST", url, this)
            .apply {
                this.init()
                this.data = data
            }
            .mergeSession(this)
            .execute()

    fun post(url: String, init: Request.() -> Unit): Response =
        Request("POST", url, this)
            .apply(init)
            .mergeSession(this)
            .execute()

    fun put(url: String, init: Request.() -> Unit): Response =
        Request("PUT", url, this)
            .apply(init)
            .mergeSession(this)
            .execute()

    fun delete(url: String, init: Request.() -> Unit): Response =
        Request("DELETE", url, this)
            .apply(init)
            .mergeSession(this)
            .execute()

    fun head(url: String, init: Request.() -> Unit): Response =
        Request("HEAD", url, this)
            .apply(init)
            .mergeSession(this)
            .execute()

    fun options(url: String, init: Request.() -> Unit): Response =
        Request("OPTIONS", url, this)
            .apply(init)
            .mergeSession(this)
            .execute()

    override fun close() {
        // kill any keepalive sockets
    }

    protected open fun newCookieManager() = CookieManager(null, CookiePolicy.ACCEPT_ALL)

}

typealias Auth = Pair<String, String>
