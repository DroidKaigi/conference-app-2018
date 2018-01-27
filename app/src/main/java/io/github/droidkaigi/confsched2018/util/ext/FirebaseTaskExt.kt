package io.github.droidkaigi.confsched2018.util.ext

import com.google.android.gms.tasks.Task
import io.reactivex.Completable
import io.reactivex.Single

fun <R> Task<R>.toSingle() = Single.create<R> { emitter ->
    this.addOnSuccessListener { emitter.onSuccess(it) }
            .addOnFailureListener { emitter.onError(it) }
}

fun <R> Task<R>.toCompletable() = Completable.create { emitter ->
    this.addOnSuccessListener { emitter.onComplete() }
            .addOnFailureListener { emitter.onError(it) }
}
