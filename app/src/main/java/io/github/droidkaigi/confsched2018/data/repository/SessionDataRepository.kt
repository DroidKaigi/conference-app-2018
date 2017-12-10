package io.github.droidkaigi.confsched2018.data.repository

import android.util.Log
import io.github.droidkaigi.confsched2018.data.api.DroidKaigiApi
import io.github.droidkaigi.confsched2018.data.db.SessionDao
import io.github.droidkaigi.confsched2018.data.entity.mapper.toSession
import io.github.droidkaigi.confsched2018.model.Session
import io.github.droidkaigi.confsched2018.util.rx.SchedulerProvider
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Flowable.just
import io.reactivex.rxkotlin.Flowables


class SessionDataRepository(
        private val api: DroidKaigiApi,
        private val db: SessionDao,
        private val schedulerProvider: SchedulerProvider
) : SessionRepository {
    override val sessions: Flowable<List<Session>> =
            Flowables.combineLatest(db.getAllSession(),
                    just(listOf<Int>()),
                    { sessionEntity, favList -> sessionEntity.toSession(favList) })
                    .subscribeOn(schedulerProvider.computation())
                    .doOnNext {
                        Log.d("SessionDataRepository", "size:" + it.size + " current:" + System.currentTimeMillis())
                    }

    override fun refreshSessions(): Completable {
        return api.getSessions()
                .doOnSuccess { db.clearAndInsert(it) }
                .subscribeOn(schedulerProvider.computation())
                .toCompletable()
    }

}
