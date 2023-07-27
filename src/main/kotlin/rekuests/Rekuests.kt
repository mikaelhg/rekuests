@file:Suppress("unused")
package rekuests

import rekuests.util.BaseRequest

fun rekuest(method: String, url: String, init: BaseRequest.() -> Unit) = Session().rekuest(method, url, init)

/** Perform a HTTP GET request. */
fun get(url: String, init: BaseRequest.() -> Unit = {}) = Session().get(url, init)

/** Perform a HTTP POST request. */
fun post(url: String, init: BaseRequest.() -> Unit = {}) = Session().post(url, init)

/** Perform a HTTP PUT request. */
fun put(url: String, init: BaseRequest.() -> Unit = {}) = Session().put(url, init)

/** Perform a HTTP PATCH request. */
fun patch(url: String, init: BaseRequest.() -> Unit = {}) = Session().patch(url, init)

/** Perform a HTTP DELETE request. */
fun delete(url: String, init: BaseRequest.() -> Unit = {}) = Session().delete(url, init)

/** Perform a HTTP HEAD request. */
fun head(url: String, init: BaseRequest.() -> Unit = {}) = Session().head(url, init)

/** Perform a HTTP OPTIONS request. */
fun options(url: String, init: BaseRequest.() -> Unit = {}) = Session().options(url, init)
