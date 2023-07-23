package rekuests.util

open class Headers : HashMap<String, String>() {

    fun update(key: String, value: String) = put(key, value)

    fun update(pair: Pair<String, String>) = put(pair.first, pair.second)

    fun update(headers: Headers): Headers {
        this.putAll(headers)
        return this
    }

}
