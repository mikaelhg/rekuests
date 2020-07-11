package rekuests

import java.io.File
import java.net.*
import java.net.CookiePolicy.ACCEPT_ALL
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse.BodyHandlers
import java.nio.charset.StandardCharsets
import java.time.Duration

class GetRequest(url: String) : Request(method = "GET", url = url)
class PostRequest(url: String) : Request(method = "POST", url = url)
class PutRequest(url: String) : Request(method = "PUT", url = url)
class DeleteRequest(url: String) : Request(method = "DELETE", url = url)
class HeadRequest(url: String) : Request(method = "HEAD", url = url)
class OptionsRequest(url: String) : Request(method = "OPTIONS", url = url)

class PreparedRequest

@Suppress("MemberVisibilityCanBePrivate")
open class Request(var method: String, var url: String) {

    var headers = Headers()

    val files = mutableMapOf<String, File>()

    var data: ByteArray? = null

    var username: String? = null

    var password: String? = null

    protected val cookieManager = CookieManager(null, ACCEPT_ALL)

    protected val queryParameters = mutableListOf<Pair<String, String>>()

    protected val dataFormFields = mutableMapOf<String, String>()

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
        this.data = data.toByteArray(StandardCharsets.ISO_8859_1)
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

    fun cookies(cookies: List<HttpCookie>) {
        val uri = URI.create(url)
        cookies.forEach { c ->
            c.domain = uri.host
            cookieManager.cookieStore.add(uri, c)
        }
    }

    fun cookies(vararg cookies: HttpCookie) {
        cookies(cookies.toList())
    }

    internal fun mergeSession(s: Session): Request {
        this.headers = Headers() + s.headers + this.headers
        return this
    }

    fun execute(): Response {

        val clientBuilder = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(20))
                .cookieHandler(cookieManager)

        val requestBuilder = HttpRequest.newBuilder()
                .method(this.method, this.bodyPublisher())
                .uri(URI.create(this.url))

        println("this.headers: $headers")

        this.headers.forEach { (k, v) -> requestBuilder.header(k, v) }

        if (null != this.username && null != this.password) {
            clientBuilder.authenticator(MyAuthenticator(this.username!!, this.password!!))
        }

        val client = clientBuilder.build()
        val request = requestBuilder.build()

        println("request.headers: ${request.headers()}")

        val response = client.send(request, BodyHandlers.ofInputStream())

        return Response(response, cookieManager.cookieStore)
    }

    protected fun bodyPublisher() = HttpRequest.BodyPublishers.noBody()

    fun prepare() = PreparedRequest()

}

internal class MyAuthenticator(private val username: String, private val password: String) : Authenticator() {
    override fun getPasswordAuthentication() = PasswordAuthentication(username, password.toCharArray())
}