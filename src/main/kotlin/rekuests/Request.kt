package rekuests

import rekuests.util.Headers
import rekuests.util.UsernamePasswordAuthenticator
import rekuests.util.timed
import java.io.File
import java.net.HttpCookie
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpClient.Redirect.NEVER
import java.net.http.HttpClient.Redirect.NORMAL
import java.net.http.HttpRequest
import java.net.http.HttpRequest.BodyPublishers
import java.net.http.HttpResponse.BodyHandlers
import java.nio.charset.StandardCharsets.UTF_8
import java.time.Duration

@Suppress("MemberVisibilityCanBePrivate")
open class Request(var method: String, var url: String, val session: Session) {

    var headers = Headers()

    val files = mutableMapOf<String, File>()

    var data: ByteArray? = null

    var username: String? = null

    var password: String? = null

    protected val queryParameters = mutableListOf<Pair<String, String>>()

    protected val dataFormFields = mutableMapOf<String, String>()

    var followRedirects = true

    var httpVersion = HttpClient.Version.HTTP_2

    var connectTimeout = Duration.ofSeconds(20)

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

    internal fun mergeSession(s: Session) = apply {
        headers = Headers().update(s.headers).update(headers)
    }

    internal fun execute(): Response {

        val uri = URI.create(this.url)

        val clientBuilder = HttpClient.newBuilder()
                .version(httpVersion)
                .followRedirects(if (followRedirects) NORMAL else NEVER)
                .connectTimeout(connectTimeout)
                .cookieHandler(session.cookieManager)

        val requestBuilder = HttpRequest.newBuilder()
                .method(this.method, this.bodyPublisher())
                .uri(uri)

        this.headers.forEach { (k, v) -> requestBuilder.header(k, v) }

        if (null != this.username && null != this.password) {
            clientBuilder.authenticator(UsernamePasswordAuthenticator(username!!, password!!))
        }

        val client = clientBuilder.build()
        val request = requestBuilder.build()

        val (response, duration) = timed {
            client.send(request, BodyHandlers.ofInputStream())
        }

        session.cookieManager.put(uri, response.headers().map())

        return Response(response, session, this).apply {
            elapsed = duration
        }
    }

    protected fun bodyPublisher() =
        data?.let(BodyPublishers::ofByteArray) ?: BodyPublishers.noBody()!!

}
