package io.github.droidkaigi.confsched2018.presentation.dispacher

import kotlinx.coroutines.experimental.channels.BroadcastChannel
import kotlinx.coroutines.experimental.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.experimental.channels.ReceiveChannel
import kotlinx.coroutines.experimental.channels.filter
import kotlinx.coroutines.experimental.channels.map
import javax.inject.Inject
import javax.inject.Singleton


/**
 * You can use like this.
 * val channel = Dispatcher().asChannel<ItemChangeAction>()
 * launch (UI){
 *   for(action in channel){
 *     // You can use item
 *     action.item
 *   }
 * }
 */
@Singleton
class Dispatcher @Inject constructor() {
    private val bus: BroadcastChannel<Any> = ConflatedBroadcastChannel<Any>()

    fun send(o: Any) {
        bus.offer(o)
    }

    inline fun <reified T> asChannel(): ReceiveChannel<T> {
        return subscription().filter { it is T }.map { it as T }
    }

    /**
     * Divide method for workaround for fail unit test(org.mockito.exceptions.misusing
     * .WrongTypeOfReturnValue)
     */
    fun subscription(): ReceiveChannel<Any> = bus.openSubscription()
}
