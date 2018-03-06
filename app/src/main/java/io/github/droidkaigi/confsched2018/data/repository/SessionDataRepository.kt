package io.github.droidkaigi.confsched2018.data.repository

import android.support.annotation.CheckResult
import android.support.annotation.VisibleForTesting
import io.github.droidkaigi.confsched2018.data.api.DroidKaigiApi
import io.github.droidkaigi.confsched2018.data.db.FavoriteDatabase
import io.github.droidkaigi.confsched2018.data.db.SessionDatabase
import io.github.droidkaigi.confsched2018.data.db.entity.mapper.toSession
import io.github.droidkaigi.confsched2018.data.db.fixeddata.SpecialSessions
import io.github.droidkaigi.confsched2018.model.Session
import io.github.droidkaigi.confsched2018.util.ext.atJST
import io.github.droidkaigi.confsched2018.util.rx.SchedulerProvider
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.rxkotlin.Flowables
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.withContext
import timber.log.Timber
import javax.inject.Inject

class SessionDataRepository @Inject constructor(
        private val api: DroidKaigiApi,
        private val sessionDatabase: SessionDatabase,
        private val favoriteDatabase: FavoriteDatabase,
        private val schedulerProvider: SchedulerProvider
) : SessionRepository {

    override val sessions: Flowable<List<Session>> =
            Flowables.combineLatest(
                    sessionDatabase.getAllSessions()
                            .filter { it.isNotEmpty() }
                            .doOnNext { if (DEBUG) Timber.d("getAllSessions") },
                    sessionDatabase.getAllSpeaker()
                            .filter { it.isNotEmpty() }
                            .doOnNext { if (DEBUG) Timber.d("getAllSpeaker") },
                    Flowable.concat(
                            Flowable.just(listOf()),
                            favoriteDatabase.favorites.onErrorReturn { listOf() }
                    )
                            .doOnNext { if (DEBUG) Timber.d("favorites") },
                    { sessionEntities, speakerEntities, favList ->
                        val firstDay = sessionEntities.first().session!!.stime.atJST().toLocalDate()
                        val speakerSessions = sessionEntities
                                .map { it.toSession(speakerEntities, favList, firstDay) }
                                .sortedWith(compareBy(
                                        { it.startTime.getTime() },
                                        { it.room.id }
                                ))

                        speakerSessions + specialSessions
                    })
                    .subscribeOn(schedulerProvider.io())
                    .doOnNext {
                        if (DEBUG) Timber.d("size:${it.size} current:${System.currentTimeMillis()}")
                    }

    @VisibleForTesting
    val specialSessions: List<Session.SpecialSession> by lazy {
        SpecialSessions.getSessions()
    }

    @CheckResult override fun favorite(session: Session.SpeechSession): Single<Boolean> =
            favoriteDatabase.favorite(session)

    @CheckResult override suspend fun refreshSessions() = withContext(CommonPool) {
        val response = api.getSessions().await()
        sessionDatabase.save(response)
    }

    companion object {
        const val DEBUG = false
    }
}
