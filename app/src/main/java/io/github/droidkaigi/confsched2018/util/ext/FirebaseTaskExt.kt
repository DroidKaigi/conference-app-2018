package io.github.droidkaigi.confsched2018.util.ext

import android.support.annotation.CheckResult
import com.google.android.gms.tasks.Task
import io.reactivex.Completable
import io.reactivex.Single

@CheckResult fun <R> Task<R>.toSingle() = Single.create<R> { emitter ->
    this.addOnSuccessListener { emitter.onSuccess(it) }
            .addOnFailureListener { emitter.onError(it) }
}

@CheckResult fun <R> Task<R>.toCompletable() = Completable.create { emitter ->
    this.addOnSuccessListener { emitter.onComplete() }
            .addOnFailureListener { emitter.onError(it) }
}
