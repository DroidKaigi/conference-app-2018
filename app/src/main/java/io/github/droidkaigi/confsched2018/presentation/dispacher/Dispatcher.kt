package io.github.droidkaigi.confsched2018.presentation.dispacher

import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.Relay
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @see https://gist.github.com/benjchristensen/04eef9ca0851f3a5d7bf
 *
 * @see https://github.com/kaushikgopal/RxJava-Android-Samples/commit
 * /dedadafd88cd3a5a926d4a7f2761822a43121569
 */

@Singleton
class Dispatcher @Inject constructor() {

    val bus: Relay<Any> = BehaviorRelay.create<Any>().toSerialized()

    fun send(o: Any) {
        Timber.d("event:" + o)
        bus.accept(o)
    }

    inline fun <reified T> asFlowable(): Flowable<T> {
        return (asAnyFlowable() as Flowable<Any>)
                .filter({ it is T })
                .map<T>({ it as T })
    }

    fun asAnyFlowable() = bus.toFlowable(BackpressureStrategy.LATEST)

    fun hasObservers(): Boolean {
        return bus.hasObservers()
    }
}
