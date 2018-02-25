package io.github.droidkaigi.confsched2018.model

sealed class LoadState {
    object Initialized : LoadState()
    object Loading : LoadState()
    object Finished : LoadState()
    data class Error(val e: Throwable) : LoadState()
}
