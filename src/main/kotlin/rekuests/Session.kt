package rekuests

import java.io.Closeable

open class Session : AutoCloseable, Closeable {

    var auth: Auth? = null

    var headers = Headers()

    fun rekuest(method: String, url: String, init: Request.() -> Unit): Response =
        Request(method, url)
            .apply(init)
            .mergeSession(this)
            .execute()

    fun get(url: String): Response =
        GetRequest(url)
            .mergeSession(this)
            .execute()

    fun get(url: String, init: GetRequest.() -> Unit): Response =
        GetRequest(url)
            .apply(init)
            .mergeSession(this)
            .execute()

    fun post(url: String, data: ByteArray, init: PostRequest.() -> Unit): Response =
        PostRequest(url)
            .apply {
                this.init()
                this.data = data
            }
            .mergeSession(this)
            .execute()

    fun post(url: String, init: PostRequest.() -> Unit): Response =
        PostRequest(url)
            .apply(init)
            .mergeSession(this)
            .execute()

    fun put(url: String, init: PutRequest.() -> Unit): Response =
        PutRequest(url)
            .apply(init)
            .mergeSession(this)
            .execute()

    fun delete(url: String, init: DeleteRequest.() -> Unit): Response =
        DeleteRequest(url)
            .apply(init)
            .mergeSession(this)
            .execute()

    fun head(url: String, init: HeadRequest.() -> Unit): Response =
        HeadRequest(url)
            .apply(init)
            .mergeSession(this)
            .execute()

    fun options(url: String, init: OptionsRequest.() -> Unit): Response =
        OptionsRequest(url)
            .apply(init)
            .mergeSession(this)
            .execute()

    override fun close() {
        // kill any keepalive sockets
    }

}

open class Headers : HashMap<String, String>() {

    fun update(key: String, value: String) = put(key, value)

    fun update(pair: Pair<String, String>) = put(pair.first, pair.second)

    operator fun plus(b: Headers): Headers {
        this.putAll(b)
        return this
    }

}

typealias Auth = Pair<String, String>
