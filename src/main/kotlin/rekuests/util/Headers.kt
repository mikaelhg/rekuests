package rekuests.util

open class Headers : HashMap<String, String>() {

    fun update(key: String, value: String) = put(key, value)

    fun update(pair: Pair<String, String>) = put(pair.first, pair.second)

    operator fun plus(b: Headers): Headers {
        this.putAll(b)
        return this
    }

}