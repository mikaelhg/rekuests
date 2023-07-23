package rekuests.util

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