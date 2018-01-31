package io.github.droidkaigi.confsched2018.presentation.common.mapper

import android.support.annotation.CheckResult
import io.github.droidkaigi.confsched2018.presentation.Result
import io.github.droidkaigi.confsched2018.util.rx.SchedulerProvider
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single

@CheckResult fun <T> Flowable<T>.toResult(schedulerProvider: SchedulerProvider):
        Flowable<Result<T>> {
    return compose { item ->
        item
                .map { Result.success(it) }
                .onErrorReturn { e -> Result.failure(e.message ?: "unknown", e) }
                .observeOn(schedulerProvider.ui())
                .startWith(Result.inProgress())
    }
}

@CheckResult fun <T> Observable<T>.toResult(schedulerProvider: SchedulerProvider):
        Observable<Result<T>> {
    return compose { item ->
        item
                .map { Result.success(it) }
                .onErrorReturn { e -> Result.failure(e.message ?: "unknown", e) }
                .observeOn(schedulerProvider.ui())
                .startWith(Result.inProgress())
    }
}

@CheckResult fun <T> Single<T>.toResult(schedulerProvider: SchedulerProvider):
        Observable<Result<T>> {
    return toObservable().toResult(schedulerProvider)
}

@CheckResult fun <T> Completable.toResult(schedulerProvider: SchedulerProvider):
        Observable<Result<T>> {
    return toObservable<T>().toResult(schedulerProvider)
}
