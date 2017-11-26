package io.github.droidkaigi.confsched2018.presentation.mapper

import io.github.droidkaigi.confsched2018.presentation.Result
import io.github.droidkaigi.confsched2018.util.rx.SchedulerProvider
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers


fun <T> Flowable<T>.toResult(schedulerProvider: SchedulerProvider): Flowable<Result<T>> =
        compose { item ->
            item
                    .map { Result.success(it) }
                    .onErrorReturn { e -> Result.failure(e.message ?: "unknown", e) }
                    .observeOn(schedulerProvider.ui())
                    .startWith(Result.inProgress())
        }

fun <T> Single<T>.toResult(): Observable<Result<T>> =
        compose { item ->
            item
                    .map { Result.success(it) }
                    .onErrorReturn { e -> Result.failure(e.message ?: "unknown", e) }
                    .observeOn(AndroidSchedulers.mainThread())
        }.toObservable().startWith(Result.inProgress())
