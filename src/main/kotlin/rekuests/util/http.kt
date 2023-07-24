package rekuests.util

import java.net.http.HttpHeaders
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets.UTF_8
import java.security.SecureRandom
import javax.net.ssl.SSLContext

internal val noValidateSecurityContext =
    SSLContext.getInstance("TLS").apply {
        init(null, arrayOf(MockTrustManager()), SecureRandom())
    }

/**
 * Parse the HTTP header "Link" contents into a result from the Requests spec.
 */
internal fun parseLinkHeaders(headers: List<String>): List<Map<String, String>> {
    val replaceChars = charArrayOf(' ', '\'', '"')
    return headers.flatMap { it.split(',') }
        .map { l ->
            val elements = l.trim().split(';')
            val url = elements[0].substring(1, elements[0].length-1)
            val map = mutableMapOf("url" to url)
            elements.listIterator(1).forEach { e ->
                val (k, v) = e.split('=', limit = 2)
                map[k.trim(*replaceChars)] = v.trim(*replaceChars)
            }
            map
        }
        .toList()
}

/**
 * The standard says ISO-8859-1 but in practise
 * implementations have switched to UTF-8.
 */
val DEFAULT_CHARSET: Charset = UTF_8

fun charsetFrom(headers: HttpHeaders): Charset {
    val optional = headers.firstValue("content-type")
    if (optional.isEmpty) return DEFAULT_CHARSET
    val raw = optional.get()
    val sci = raw.indexOf(';')
    val csi = raw.indexOf("charset=", ignoreCase = true)
    if (sci == -1 || csi == -1 || csi < sci) return DEFAULT_CHARSET
    return Charset.forName(raw.substring(csi+8))
}
