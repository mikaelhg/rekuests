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
        GetRequest(url, this)
            .mergeSession(this)
            .execute()

    fun get(url: String, init: GetRequest.() -> Unit): Response =
        GetRequest(url, this)
            .apply(init)
            .mergeSession(this)
            .execute()

    fun post(url: String, data: ByteArray, init: PostRequest.() -> Unit): Response =
        PostRequest(url, this)
            .apply {
                this.init()
                this.data = data
            }
            .mergeSession(this)
            .execute()

    fun post(url: String, init: PostRequest.() -> Unit): Response =
        PostRequest(url, this)
            .apply(init)
            .mergeSession(this)
            .execute()

    fun put(url: String, init: PutRequest.() -> Unit): Response =
        PutRequest(url, this)
            .apply(init)
            .mergeSession(this)
            .execute()

    fun delete(url: String, init: DeleteRequest.() -> Unit): Response =
        DeleteRequest(url, this)
            .apply(init)
            .mergeSession(this)
            .execute()

    fun head(url: String, init: HeadRequest.() -> Unit): Response =
        HeadRequest(url, this)
            .apply(init)
            .mergeSession(this)
            .execute()

    fun options(url: String, init: OptionsRequest.() -> Unit): Response =
        OptionsRequest(url, this)
            .apply(init)
            .mergeSession(this)
            .execute()

    override fun close() {
        // kill any keepalive sockets
    }

    protected open fun newCookieManager() = CookieManager(null, CookiePolicy.ACCEPT_ALL)

}

typealias Auth = Pair<String, String>
