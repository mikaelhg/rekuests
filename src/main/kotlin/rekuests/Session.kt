package rekuests

open class Session {

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

}