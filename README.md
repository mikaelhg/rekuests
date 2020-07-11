# rekuests

The Python [requests](https://requests.readthedocs.io/) library for Kotlin.

Under construction.

\[Imagine a '90s Geocities animated "Under construction" GIF here.\]

```kotlin
import rekuests

val r = rekuests.get("https://api.github.com/user") {
    auth("user", "pass")
}

r.status_code

r.headers["content-type"]

r.text

r.json()
```

```kotlin
import rekuests

val s = rekuests.Session()

s.auth = "user" to "pass"
s.headers.update("x-test" to "true")
s.headers["foo"] = "bar"

s.get("https://httpbin.org/headers") {
    headers("x-test2" to "true")
}

var r = s.get("https://httpbin.org/cookies") {
    cookie("from-my" to "browser")
}
println(r.text)
// '{"cookies": {"from-my": "browser"}}'

r = s.get("https://httpbin.org/cookies")
println(r.text)
// '{"cookies": {}}'

rekuests.Session().use { s -> 
    s.get("https://httpbin.org/cookies/set/sessioncookie/123456789")
}
```
