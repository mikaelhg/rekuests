@file:Suppress("MemberVisibilityCanBePrivate", "unused")
package rekuests.util

import rekuests.Session
import rekuests.auth.UsernamePasswordAuthenticator
import java.io.File
import java.net.Authenticator
import java.net.HttpCookie
import java.net.ProxySelector
import java.net.URI
import java.net.http.HttpClient
import java.time.Duration

/**
 * Only stuff we want to publish to request initializers.
 */
abstract class BaseRequest(var url: String, val session: Session) {

    var headers = Headers()

    val files = mutableMapOf<String, File>()

    var data: ByteArray? = null

    var username: String? = null

    var password: String? = null

    var authenticator: Authenticator? = null

    var followRedirects = true

    var httpVersion = HttpClient.Version.HTTP_2

    var connectTimeout: Duration = Duration.ofSeconds(20)

    var verifyCertificate = true

    var proxySelector = ProxySelector.getDefault()!!

    fun headers(vararg headers: Pair<String, String>) {
        headers.forEach { (k, v) -> this.headers[k] = v }
    }

    fun files(vararg files: Pair<String, File>) {
        files.forEach { (k, v) -> this.files[k] = v }
    }

    fun data(data: ByteArray) {
        this.data = data
    }

    fun data(data: String) {
        this.data = data.toByteArray(Charsets.UTF_8)
    }

    abstract fun data(data: Map<String, String>)

    abstract fun data(vararg data: Pair<String, String>)

    abstract fun params(params: Map<String, String>)

    abstract fun params(vararg params: Pair<String, String>)

    fun auth(username: String, password: String) {
        this.username = username
        this.password = password
        this.authenticator = UsernamePasswordAuthenticator(username, password)
    }

    fun auth(authenticator: Authenticator) {
        this.authenticator = authenticator
    }

    fun cookie(key: String, value: String, path: String = "/", secure: Boolean = false) {
        val uri = URI.create(url)
        val cookie = HttpCookie(key, value).apply {
            this.path = path
            this.domain = uri.host
            this.secure = secure
        }
        session.cookieManager.cookieStore.add(uri, cookie)
    }

    fun cookies(cookies: List<HttpCookie>) {
        val uri = URI.create(url)
        cookies.forEach { c ->
            c.domain = uri.host
            session.cookieManager.cookieStore.add(uri, c)
        }
    }

    fun cookies(vararg cookies: HttpCookie) {
        cookies(cookies.toList())
    }

}
