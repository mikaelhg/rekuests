@file:Suppress("MemberVisibilityCanBePrivate", "unused")
package rekuests

import rekuests.auth.BasicAuth
import rekuests.util.BaseRequest
import rekuests.util.Headers
import java.io.Closeable
import java.net.CookieManager
import java.net.CookiePolicy.ACCEPT_ALL

open class Session : AutoCloseable, Closeable {

    var auth: BasicAuth? = null

    var headers = Headers()

    val cookieManager by lazy { newCookieManager() }

    fun rekuest(method: String, url: String, init: BaseRequest.() -> Unit = {}): Response =
        Request(method, url, this)
            .apply(init)
            .mergeSession()
            .execute()

    fun get(url: String, init: BaseRequest.() -> Unit = {}) = rekuest("GET", url, init)

    fun post(url: String, init: BaseRequest.() -> Unit) = rekuest("POST", url, init)

    fun put(url: String, init: BaseRequest.() -> Unit) = rekuest("PUT", url, init)

    fun patch(url: String, init: BaseRequest.() -> Unit) = rekuest("PATCH", url, init)

    fun delete(url: String, init: BaseRequest.() -> Unit) = rekuest("DELETE", url, init)

    fun head(url: String, init: BaseRequest.() -> Unit) = rekuest("HEAD", url, init)

    fun options(url: String, init: BaseRequest.() -> Unit) = rekuest("OPTIONS", url, init)

    override fun close() {
        TODO("add socket and client management")
    }

    protected open fun newCookieManager() = CookieManager(null, ACCEPT_ALL)

}
