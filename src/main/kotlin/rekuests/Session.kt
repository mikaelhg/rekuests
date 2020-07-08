package rekuests

open class Session {

    fun rekuest(method: String, url: String, init: Request.() -> Unit): Response
            = Request(method, url).apply(init).execute()

    fun get(url: String, init: GetRequest.() -> Unit): Response
            = GetRequest(url).apply(init).execute()

    fun post(url: String, init: PostRequest.() -> Unit): Response
            = PostRequest(url).apply(init).execute()

    fun put(url: String, init: PutRequest.() -> Unit): Response
            = PutRequest(url).apply(init).execute()

    fun delete(url: String, init: DeleteRequest.() -> Unit): Response
            = DeleteRequest(url).apply(init).execute()

    fun head(url: String, init: HeadRequest.() -> Unit): Response
            = HeadRequest(url).apply(init).execute()

    fun options(url: String, init: OptionsRequest.() -> Unit): Response
            = OptionsRequest(url).apply(init).execute()

}