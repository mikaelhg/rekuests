@file:Suppress("MemberVisibilityCanBePrivate", "unused")
package rekuests

import rekuests.util.Headers
import rekuests.auth.UsernamePasswordAuthenticator
import rekuests.util.noValidateSecurityContext
import rekuests.util.timed
import java.io.File
import java.net.Authenticator
import java.net.HttpCookie
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpClient.Redirect.NEVER
import java.net.http.HttpClient.Redirect.NORMAL
import java.net.http.HttpRequest
import java.net.http.HttpRequest.BodyPublisher
import java.net.http.HttpRequest.BodyPublishers
import java.net.http.HttpResponse.BodyHandlers
import java.nio.charset.StandardCharsets.UTF_8
import java.time.Duration

open class Request(var method: String, var url: String, val session: Session) {

    var headers = Headers()

    val files = mutableMapOf<String, File>()

    var data: ByteArray? = null

    var username: String? = null

    var password: String? = null

    protected var authenticator: Authenticator? = null

    protected val queryParameters = mutableListOf<Pair<String, String>>()

    protected val dataFormFields = mutableMapOf<String, String>()

    var followRedirects = true

    var httpVersion = HttpClient.Version.HTTP_2

    var connectTimeout: Duration = Duration.ofSeconds(20)

    val verifyCertificate = true

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
        this.data = data.toByteArray(UTF_8)
    }

    fun data(data: Map<String, String>) {
        this.dataFormFields.putAll(data)
    }

    fun data(vararg data: Pair<String, String>) {
        data.forEach { (k, v) -> this.dataFormFields[k] = v }
    }

    fun params(params: Map<String, String>) {
        params.entries.forEach { (k, v) -> this.queryParameters.add(Pair(k, v)) }
    }

    fun params(vararg params: Pair<String, String>) {
        this.queryParameters.addAll(params)
    }

    fun auth(username: String, password: String) {
        this.username = username
        this.password = password
        this.authenticator = UsernamePasswordAuthenticator(username, password)
    }

    fun auth(authenticator: Authenticator) {
        this.authenticator = authenticator
    }

    fun cookie(key: String, value: String) {
        val uri = URI.create(url)
        val cookie = HttpCookie(key, value).apply {
            path = "/"
            domain = uri.host
            secure = false
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

    protected fun mergeSession() = apply {
        headers = Headers().update(session.headers).update(headers)
    }

    protected fun execute(): Response {
        val uri = URI.create(url)
        val (httpResponse, duration) = timed {
            httpClient().send(httpRequest(uri), BodyHandlers.ofInputStream())
        }
        session.cookieManager.put(uri, httpResponse.headers().map())
        return Response(httpResponse, session, this).apply {
            elapsed = duration
        }
    }

    protected fun httpRequest(uri: URI): HttpRequest =
        HttpRequest.newBuilder()
            .method(method, bodyPublisher())
            .uri(uri)
            .apply { headers.forEach(::header) }
            .build()

    protected fun httpClient(): HttpClient =
        HttpClient.newBuilder()
            .version(httpVersion)
            .followRedirects(if (followRedirects) NORMAL else NEVER)
            .connectTimeout(connectTimeout)
            .cookieHandler(session.cookieManager)
            .apply {
                if (!verifyCertificate) {
                    sslContext(noValidateSecurityContext)
                }
                authenticator?.let(::authenticator)
            }
            .build()

    /*
     * https://mizosoft.github.io/methanol/multipart_and_forms/
     */
    protected fun bodyPublisher(): BodyPublisher =
        if (files.isNotEmpty() && dataFormFields.isNotEmpty()) {
            BodyPublishers.noBody() // XXX: FIXME mime multipart body
        } else if (dataFormFields.isNotEmpty()) {
            BodyPublishers.noBody() // XXX: FIXME mime multipart body
        } else if (null != data) {
            BodyPublishers.ofByteArray(data)
        } else {
            BodyPublishers.noBody()
        }

}
