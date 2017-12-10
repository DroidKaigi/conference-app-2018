package io.github.droidkaigi.confsched2018.util.ext

import android.arch.lifecycle.LiveDataReactiveStreams
import org.reactivestreams.Publisher


fun <T> Publisher<T>.toLiveData() = LiveDataReactiveStreams.fromPublisher(this)
