@file:Suppress("MemberVisibilityCanBePrivate", "unused")
package rekuests

import rekuests.util.RekuestException
import rekuests.util.parseLinkHeaders
import java.io.InputStream
import java.net.HttpCookie
import java.net.http.HttpResponse
import java.nio.charset.Charset
import java.time.Duration
import java.util.stream.Stream

@Suppress("MemberVisibilityCanBePrivate", "UNUSED_PARAMETER")
open class Response(
    protected val httpResponse: HttpResponse<InputStream>,
    protected val session: Session,
    protected val request: Request
)
{

    /**
     * Case-insensitive Dictionary of Response Headers. For example,
     * headers['content-encoding'] will return the value of a 'Content-Encoding'
     * response header.
     */
    val headers: Map<String, List<String>> by lazy { httpResponse.headers().map() }

    /**
     * Encoding to decode with when accessing r.text.
     */
    var encoding: String = "UTF-8"

    /**
     * Content of the response, in unicode.
     *
     * If Response.encoding is None, encoding will be guessed using chardet.
     *
     * The encoding of the response content is determined based solely on HTTP headers, following RFC 2616 to the
     * letter. If you can take advantage of non-HTTP knowledge to make a better guess at the encoding, you should
     * set r.encoding appropriately before accessing this property.
     */
    val text: String by lazy { content.toString(Charset.forName(encoding)) }

    /**
     * The apparent encoding, provided by the chardet library.
     */
    val apparentEncoding = "asd"

    /**
     * Content of the response, in bytes.
     */
    val content: ByteArray by lazy { httpResponse.body().readAllBytes() }

    /**
     * A CookieJar of Cookies the server sent back.
     */
    val cookies: List<HttpCookie> by lazy { session.cookieManager.cookieStore.cookies }

    /**
     * The amount of time elapsed between sending the request and the arrival of the response
     * (as a Duration). This property specifically measures the time taken between sending the
     * first byte of the request and finishing parsing the headers. It is therefore unaffected
     * by consuming the response content or the value of the stream keyword argument.
     */
    var elapsed: Duration = Duration.ofSeconds(0)

    /**
     * A list of HttpResponse objects from the history of the Request.
     * Any redirect responses will end up here.
     * The list is sorted from the oldest to the most recent request.
     */
    val history: List<HttpResponse<InputStream>>
        get() {
            val ret = mutableListOf<HttpResponse<InputStream>>()
            var optResponse = httpResponse.previousResponse()
            while (optResponse.isPresent) {
                val r = optResponse.get()
                ret.add(0, r)
                optResponse = r.previousResponse()
            }
            return ret
        }

    /**
     * True if this Response one of the permanent versions of redirect.
     */
    val isPermanentRedirect by lazy {
        "location" in headers && statusCode in setOf(301, 308)
    }

    /**
     * True if this Response is a well-formed HTTP redirect that could have been processed automatically
     * (by Session.resolve_redirects).
     */
    val isRedirect by lazy {
        "location" in headers && statusCode in setOf(301, 302, 303, 307, 308)
    }

    /**
     * Iterates over the response data. When stream=True is set on the request, this avoids reading
     * the content at once into memory for large responses. The chunk size is the number of bytes
     * it should read into memory.
     * This is not necessarily the length of each item returned as decoding can take place.
     *
     * chunk_size must be of type int or None. A value of None will function differently depending
     * on the value of stream. stream=True will read data as it arrives in whatever size the chunks
     * are received. If stream=False, data is returned as a single chunk.
     *
     * If decode_unicode is True, content will be decoded using the best available encoding
     * based on the response.
     */
    fun iterateContent(chunkSize: Int = 1) : Stream<ByteArray> {
        TODO("implement method")
    }

    /**
     * Iterates over the response data, one line at a time. When stream=True is set on the request,
     * this avoids reading the content at once into memory for large responses.
     */
    fun iterateLines(chunkSize: Int = 512, delimiter: String = "\r\n") : Stream<String> {
        TODO("implement method")
    }

    /**
     * Returns the json-encoded content of a response, if any.
     *
     * @throws SerializationException If the response body does not contain valid json.
     */
    inline fun <reified T> json(): T = TODO("think about JSON a bit")

    /**
     * Returns the parsed header links of the response, if any.
     *
     * https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Link
     */
    val links by lazy { headers["link"]?.let(::parseLinkHeaders) ?: emptyList() }

    /**
     * Returns a PreparedRequest for the next request in a redirect chain, if there is one.
     */
    fun next() { }

    /**
     * Returns True if statusCode is less than 400, False if not.
     *
     * This attribute checks if the status code of the response is between 400 and 600 to see
     * if there was a client error or a server error. If the status code is between 200 and 400,
     * this will return True.  This is not a check to see if the response code is 200 OK.
     */
    fun ok() = statusCode < 400

    /**
     * Raises RekuestException, if one occurred.
     * @throws RekuestException
     */
    @Throws(RekuestException::class)
    fun raiseForStatus() {
        TODO("implement method")
    }

    /**
     * File-like object representation of response (for advanced usage).
     * Use of raw requires that stream=True be set on the request.
     * This requirement does not apply for use internally to Requests.
     */
    fun raw() = httpResponse

    /**
     * Textual reason of responded HTTP Status, e.g. “Not Found” or “OK”.
     */
    fun reason() = httpResponse.statusCode()

    /**
     * Integer Code of responded HTTP Status, e.g. 404 or 200.
     */
    val statusCode: Int by lazy { httpResponse.statusCode() }

    override fun toString() = "<Response [$statusCode]>"

}
