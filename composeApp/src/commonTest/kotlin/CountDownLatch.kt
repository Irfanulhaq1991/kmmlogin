import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume

class CountDownLatch(
    initialCount: Int
) {
    private var count = initialCount
    private val mutex = Mutex()
    private val awaiters = mutableSetOf<Continuation<Unit>>()

    suspend fun countDown() {
        if (count == 0) {
            return
        }
        mutex.withLock {
            if (count == 0) {
                return
            }
            count--
            if (count == 0) {
                releaseAll()
            }
        }
    }

    private fun releaseAll() {
        for (awaiter in awaiters) {
            awaiter.resume(Unit)
        }
    }

    suspend fun await() {
        if (count == 0) {
            return
        }
        mutex.withLock {
            if (count == 0) {
                return
            }
        }
        suspendCancellableCoroutine{
            awaiters.add(it)
        }
    }
}