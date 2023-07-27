package rekuests.util

import java.time.Duration

internal fun <T> timed(func: () -> T): Pair<T, Duration> {
    val t0 = System.nanoTime()
    val response = func()
    val t1 = System.nanoTime()
    return Pair(response, Duration.ofNanos(t1 - t0))
}