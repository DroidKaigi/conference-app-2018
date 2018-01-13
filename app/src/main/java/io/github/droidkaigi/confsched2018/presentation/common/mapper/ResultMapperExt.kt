package io.github.droidkaigi.confsched2018.presentation.common.mapper

import io.github.droidkaigi.confsched2018.presentation.Result
import io.github.droidkaigi.confsched2018.util.rx.SchedulerProvider
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single

fun <T> Flowable<T>.toResult(schedulerProvider: SchedulerProvider): Flowable<Result<T>> {
    return compose { item ->
        item
                .map { Result.success(it) }
                .onErrorReturn { e -> Result.failure(e.message ?: "unknown", e) }
                .observeOn(schedulerProvider.ui())
                .startWith(Result.inProgress())
    }
}

fun <T> Observable<T>.toResult(schedulerProvider: SchedulerProvider): Observable<Result<T>> {
    return compose { item ->
        item
                .map { Result.success(it) }
                .onErrorReturn { e -> Result.failure(e.message ?: "unknown", e) }
                .observeOn(schedulerProvider.ui())
                .startWith(Result.inProgress())
    }
}

fun <T> Single<T>.toResult(schedulerProvider: SchedulerProvider): Observable<Result<T>> {
    return toObservable().toResult(schedulerProvider)
}


fun <T> Completable.toResult(schedulerProvider: SchedulerProvider): Observable<Result<T>> {
    return toObservable<T>().toResult(schedulerProvider)
}
