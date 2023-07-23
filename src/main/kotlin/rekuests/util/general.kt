package rekuests.util

import java.time.Duration
import java.time.Instant

internal fun <T> timed(func: () -> T): Pair<T, Duration> {
    val t0 = Instant.now()
    val response = func()
    val t1 = Instant.now()
    return Pair(response, Duration.between(t0, t1))
}