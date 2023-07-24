@file:Suppress("unused")
package rekuests

fun rekuest(method: String, url: String, init: Request.() -> Unit) = Session().rekuest(method, url, init)

fun get(url: String, init: Request.() -> Unit = {}) = Session().get(url, init)

fun post(url: String, init: Request.() -> Unit = {}) = Session().post(url, init)

fun put(url: String, init: Request.() -> Unit = {}) = Session().put(url, init)

fun patch(url: String, init: Request.() -> Unit = {}) = Session().patch(url, init)

fun delete(url: String, init: Request.() -> Unit = {}) = Session().delete(url, init)

fun head(url: String, init: Request.() -> Unit = {}) = Session().head(url, init)

fun options(url: String, init: Request.() -> Unit = {}) = Session().options(url, init)
