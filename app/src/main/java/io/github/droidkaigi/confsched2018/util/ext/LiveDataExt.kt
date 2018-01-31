package io.github.droidkaigi.confsched2018.util.ext

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.Transformations

fun <T> LiveData<T>.observe(owner: LifecycleOwner, observer: (T?) -> Unit) where T : Any =
        observe(owner, Observer<T> { v -> observer(v) })

fun <X, Y> LiveData<X>.map(transformer: (X) -> Y): LiveData<Y> =
        Transformations.map(this, transformer)

fun <X, Y> LiveData<X>.switchMap(transformer: (X) -> LiveData<Y>): LiveData<Y> =
        Transformations.switchMap(this, transformer)
