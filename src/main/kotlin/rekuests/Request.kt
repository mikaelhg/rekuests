@file:Suppress("MemberVisibilityCanBePrivate", "unused")
package rekuests

import com.github.mizosoft.methanol.FormBodyPublisher
import com.github.mizosoft.methanol.Methanol
import com.github.mizosoft.methanol.MultipartBodyPublisher
import io.mikael.urlbuilder.UrlBuilder
import rekuests.util.BaseRequest
import rekuests.util.Headers
import rekuests.util.noValidateSecurityContext
import rekuests.util.timed
import java.net.ProxySelector
import java.net.URI
import java.net.http.HttpClient.Redirect.NEVER
import java.net.http.HttpClient.Redirect.NORMAL
import java.net.http.HttpRequest
import java.net.http.HttpRequest.BodyPublisher
import java.net.http.HttpRequest.BodyPublishers
import java.net.http.HttpResponse.BodyHandlers

open class Request(
    var method: String, url: String, session: Session
) : BaseRequest(url, session) {

    protected val queryParameters = mutableListOf<Pair<String, String>>()

    protected val dataFormFields = mutableMapOf<String, String>()

    override fun data(data: Map<String, String>) {
        this.dataFormFields.putAll(data)
    }

    override fun data(vararg data: Pair<String, String>) {
        data.forEach { (k, v) -> this.dataFormFields[k] = v }
    }

    override fun params(params: Map<String, String>) {
        params.forEach { (k, v) -> this.queryParameters.add(Pair(k, v)) }
    }

    override fun params(vararg params: Pair<String, String>) {
        this.queryParameters.addAll(params)
    }

    internal fun mergeSession() = apply {
        headers = Headers().update(session.headers).update(headers)
    }

    internal fun execute(): Response {
        val uri = uri()
        val (httpResponse, duration) = timed {
            httpClient().use {
                it.send(httpRequest(uri), BodyHandlers.ofInputStream())
            }
        }
        session.cookieManager.put(uri, httpResponse.headers().map())
        return Response(httpResponse, session, this).apply {
            elapsed = duration
        }
    }

    protected fun uri(): URI =
        UrlBuilder.fromString(url).let {
            queryParameters.fold(it) { b, (k, v) -> b.addParameter(k, v) }
        }.toUri()

    protected fun httpRequest(uri: URI): HttpRequest =
        HttpRequest.newBuilder()
            .method(method, bodyPublisher())
            .uri(uri)
            .apply { headers.forEach(::header) }
            .build()

    protected fun httpClient(): Methanol =
        Methanol.newBuilder()
            .version(httpVersion)
            .followRedirects(if (followRedirects) NORMAL else NEVER)
            .connectTimeout(connectTimeout)
            .cookieHandler(session.cookieManager)
            .proxy(proxySelector)
            .apply {
                authenticator?.let(it::authenticator)
                if (!verifyCertificate) {
                    it.sslContext(noValidateSecurityContext)
                }
            }
            .build()

    /*
     * https://mizosoft.github.io/methanol/multipart_and_forms/
     */
    protected fun bodyPublisher(): BodyPublisher =
        when {
            files.isNotEmpty() -> {
                MultipartBodyPublisher.newBuilder().apply {
                    files.forEach { (name, file) -> filePart(name, file.toPath()) }
                }.build()
            }
            dataFormFields.isNotEmpty() -> {
                FormBodyPublisher.newBuilder().apply {
                    dataFormFields.forEach(::query)
                }.build()
            }
            null != data -> BodyPublishers.ofByteArray(data)
            else -> BodyPublishers.noBody()
        }

}
