@file:Suppress("MemberVisibilityCanBePrivate", "unused")
package rekuests

import rekuests.util.BaseRequest
import rekuests.util.Headers
import rekuests.util.noValidateSecurityContext
import rekuests.util.timed
import java.net.URI
import java.net.http.HttpClient
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
        params.entries.forEach { (k, v) -> this.queryParameters.add(Pair(k, v)) }
    }

    override fun params(vararg params: Pair<String, String>) {
        this.queryParameters.addAll(params)
    }

    internal fun mergeSession() = apply {
        headers = Headers().update(session.headers).update(headers)
    }

    internal fun execute(): Response {
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
