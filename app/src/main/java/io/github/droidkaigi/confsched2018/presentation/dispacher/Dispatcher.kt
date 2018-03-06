package io.github.droidkaigi.confsched2018.presentation.dispacher

import kotlinx.coroutines.experimental.channels.BroadcastChannel
import kotlinx.coroutines.experimental.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.experimental.channels.ReceiveChannel
import kotlinx.coroutines.experimental.channels.filter
import kotlinx.coroutines.experimental.channels.map
import kotlinx.coroutines.experimental.launch
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
    val bus: BroadcastChannel<Any> = ConflatedBroadcastChannel<Any>()

    fun send(o: Any) {
        launch {
            bus.send(o)
        }
    }

    inline fun <reified T> asChannel(): ReceiveChannel<T> {
        return bus.openSubscription().filter { it is T }.map { it as T }
    }
}
