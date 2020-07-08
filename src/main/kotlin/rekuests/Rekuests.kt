package rekuests

import java.io.File
import java.time.Duration

fun rekuest(method: String, url: String, init: RekuestContainer.() -> Unit): Response
        = RekuestContainer(method, url).apply(init).execute()

fun get(url: String, init: GetContainer.() -> Unit): Response
        = GetContainer(url).apply(init).execute()

fun post(url: String, init: PostContainer.() -> Unit): Response
        = PostContainer(url).apply(init).execute()

fun put(url: String, init: PutContainer.() -> Unit): Response
        = PutContainer(url).apply(init).execute()

fun delete(url: String, init: DeleteContainer.() -> Unit): Response
        = DeleteContainer(url).apply(init).execute()

fun head(url: String, init: HeadContainer.() -> Unit): Response
        = HeadContainer(url).apply(init).execute()

fun options(url: String, init: OptionsContainer.() -> Unit): Response
        = OptionsContainer(url).apply(init).execute()

open class Response {

    /**
     * Case-insensitive Dictionary of Response Headers. For example, headers['content-encoding'] will return the
     * value of a 'Content-Encoding' response header.
     */
    var headers = mutableMapOf<String, String>()

    /**
     * Encoding to decode with when accessing r.text.
     */
    var encoding: String = "utf-8"

    /**
     * Content of the response, in unicode.
     *
     * If Response.encoding is None, encoding will be guessed using chardet.
     *
     * The encoding of the response content is determined based solely on HTTP headers, following RFC 2616 to the
     * letter. If you can take advantage of non-HTTP knowledge to make a better guess at the encoding, you should
     * set r.encoding appropriately before accessing this property.
     */
    var text = """{"foo": "bar"}"""

    /**
     * The apparent encoding, provided by the chardet library.
     */
    var apparent_encoding = "asd"

    /**
     * Content of the response, in bytes.
     */
    var content = byteArrayOf(1, 2, 3, 4, 5)

    /**
     * A CookieJar of Cookies the server sent back.
     */
    var cookies = mutableListOf<String>()

    /**
     * The amount of time elapsed between sending the request and the arrival of the response (as a timedelta).
     * This property specifically measures the time taken between sending the first byte of the request and
     * finishing parsing the headers. It is therefore unaffected by consuming the response content or the
     * value of the stream keyword argument.
     */
    var elapsed: Duration = Duration.ofSeconds(5)

    /**
     * A list of Response objects from the history of the Request. Any redirect responses will end up here.
     * The list is sorted from the oldest to the most recent request.
     */
    var history = mutableListOf<String>()

    /**
     * True if this Response one of the permanent versions of redirect.
     */
    var is_permanent_redirect = false

    /**
     * True if this Response is a well-formed HTTP redirect that could have been processed automatically
     * (by Session.resolve_redirects).
     */
    var is_redirect = false

    /**
     * Iterates over the response data. When stream=True is set on the request, this avoids reading the content
     * at once into memory for large responses. The chunk size is the number of bytes it should read into memory.
     * This is not necessarily the length of each item returned as decoding can take place.
     *
     * chunk_size must be of type int or None. A value of None will function differently depending on the value
     * of stream. stream=True will read data as it arrives in whatever size the chunks are received.
     * If stream=False, data is returned as a single chunk.
     *
     * If decode_unicode is True, content will be decoded using the best available encoding based on the response.
     */
    fun iter_content(chunk_size: Int = 1, decode_unicode: Boolean = false) { }

    /**
     * Iterates over the response data, one line at a time. When stream=True is set on the request, this avoids
     * reading the content at once into memory for large responses.
     */
    fun iter_lines(chunk_size: Int = 512, decode_unicode: Boolean = false, delimiter: String?) { }

    /**
     * Returns the json-encoded content of a response, if any.
     * @throws ValueError If the response body does not contain valid json.
     */
    fun json() = mapOf("foo" to "bar")

    /**
     * Returns the parsed header links of the response, if any.
     */
    var links = mutableListOf<String>()

    /**
     * Returns a PreparedRequest for the next request in a redirect chain, if there is one.
     */
    fun next() { }

    /**
     * Returns True if status_code is less than 400, False if not.
     *
     * This attribute checks if the status code of the response is between 400 and 600 to see if there was a
     * client error or a server error. If the status code is between 200 and 400, this will return True.
     * This is not a check to see if the response code is 200 OK.
     */
    fun ok() = status_code < 400

    /**
     * Raises HTTPError, if one occurred.
     */
    // @Throws(HTTPError)
    fun raise_for_status() { }

    /**
     * File-like object representation of response (for advanced usage).
     * Use of raw requires that stream=True be set on the request.
     * This requirement does not apply for use internally to Requests.
     */
    fun raw() { }

    /**
     * Textual reason of responded HTTP Status, e.g. “Not Found” or “OK”.
     */
    fun reason() = "OK"

    /**
     * Integer Code of responded HTTP Status, e.g. 404 or 200.
     */
    var status_code: Int = 200

    override fun toString() = "<Response [$status_code]>"

}

class RekuestContainer(method: String, url: String) : Request(method = method, url = url)
class GetContainer(url: String) : Request(method = "GET", url = url)
class PostContainer(url: String) : Request(method = "POST", url = url)
class PutContainer(url: String) : Request(method = "PUT", url = url)
class DeleteContainer(url: String) : Request(method = "DELETE", url = url)
class HeadContainer(url: String) : Request(method = "HEAD", url = url)
class OptionsContainer(url: String) : Request(method = "OPTIONS", url = url)

open class Request(var method: String, var url: String) {

    fun headers(vararg headers: Pair<String, String>) { }

    fun files(vararg files: File) { }

    fun data(data: ByteArray) { }

    fun data(data: String) { }

    fun data(data: Map<String, Any>) { }

    fun data(vararg data: Pair<String, Any>) { }

    fun params(params: Map<String, Any>) { }

    fun params(vararg params: Pair<String, Any>) { }

    fun auth(username: String, password: String) { }

    fun cookies(cookieJar: CookieJar) {}

    fun cookies(vararg params: Pair<String, Any>) { }

    fun execute() = Response()

    fun prepare() = PreparedRequest()

}

open class CookieJar

class PreparedRequest {

}

class RequestContainer {

}