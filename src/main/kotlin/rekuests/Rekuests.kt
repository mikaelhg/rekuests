package rekuests

fun rekuest(method: String, url: String, init: Request.() -> Unit) = Session().rekuest(method, url, init)

fun get(url: String, init: GetRequest.() -> Unit = {}) = Session().get(url, init)

fun post(url: String, init: PostRequest.() -> Unit) = Session().post(url, init)

fun put(url: String, init: PutRequest.() -> Unit) = Session().put(url, init)

fun delete(url: String, init: DeleteRequest.() -> Unit) = Session().delete(url, init)

fun head(url: String, init: HeadRequest.() -> Unit) = Session().head(url, init)

fun options(url: String, init: OptionsRequest.() -> Unit) = Session().options(url, init)
