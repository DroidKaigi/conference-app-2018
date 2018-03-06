package io.github.droidkaigi.confsched2018.util.ext

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.channels.ReceiveChannel
import kotlinx.coroutines.experimental.launch

inline fun <reified E> ReceiveChannel<E>.toLiveData(): LiveData<E> {
    val receiveChannel = this
    return object : MutableLiveData<E>() {
        init {
            launch(UI) {
                for (item in receiveChannel) {
                    value = item
                }
            }
        }
    }
}
