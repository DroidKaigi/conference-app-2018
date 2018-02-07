package io.github.droidkaigi.confsched2018.util.ext

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.Transformations

inline fun <T> LiveData<T>.observe(
        owner: LifecycleOwner,
        crossinline observer: (T?) -> Unit
) {
    observe(owner, Observer<T> { v -> observer(v) })
}

inline fun <T> LiveData<T>.observeNonNull(
        owner: LifecycleOwner,
        crossinline observer: (T) -> Unit
) {
    this.observe(owner, Observer {
        if (it != null) {
            observer(it)
        }
    })
}

inline fun <X, Y> LiveData<X>.map(crossinline transformer: (X) -> Y): LiveData<Y> =
        Transformations.map(this, { transformer(it) })

inline fun <X, Y> LiveData<X>.switchMap(crossinline transformer: (X) -> LiveData<Y>): LiveData<Y> =
        Transformations.switchMap(this, { transformer(it) })
