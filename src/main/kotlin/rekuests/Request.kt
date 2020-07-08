package rekuests

import java.io.File
import java.nio.charset.StandardCharsets

class GetRequest(url: String) : Request(method = "GET", url = url)
class PostRequest(url: String) : Request(method = "POST", url = url)
class PutRequest(url: String) : Request(method = "PUT", url = url)
class DeleteRequest(url: String) : Request(method = "DELETE", url = url)
class HeadRequest(url: String) : Request(method = "HEAD", url = url)
class OptionsRequest(url: String) : Request(method = "OPTIONS", url = url)

class PreparedRequest

open class Request(var method: String, var url: String) {

    val headers = mutableMapOf<String, String>()

    val files = mutableMapOf<String, File>()

    var data: ByteArray? = null

    var username: String? = null

    var password: String? = null

    var cookies = CookieJar()

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

    fun cookies(cookies: CookieJar) {
        this.cookies = cookies
    }

    fun cookies(vararg params: Pair<String, Any>) { }

    fun execute() = Response()

    fun prepare() = PreparedRequest()

}