package io.github.droidkaigi.confsched2018.data.repository

import android.arch.persistence.room.RoomDatabase
import io.github.droidkaigi.confsched2018.data.api.DroidKaigiApi
import io.github.droidkaigi.confsched2018.data.api.response.mapper.toSessionEntities
import io.github.droidkaigi.confsched2018.data.api.response.mapper.toSessionSpeakerJoinEntities
import io.github.droidkaigi.confsched2018.data.api.response.mapper.toSpeakerEntities
import io.github.droidkaigi.confsched2018.data.db.FavoriteDatabase
import io.github.droidkaigi.confsched2018.data.db.dao.SessionDao
import io.github.droidkaigi.confsched2018.data.db.dao.SessionSpeakerJoinDao
import io.github.droidkaigi.confsched2018.data.db.dao.SpeakerDao
import io.github.droidkaigi.confsched2018.data.db.entity.mapper.toRooms
import io.github.droidkaigi.confsched2018.data.db.entity.mapper.toSession
import io.github.droidkaigi.confsched2018.data.db.entity.mapper.toSpeaker
import io.github.droidkaigi.confsched2018.model.Room
import io.github.droidkaigi.confsched2018.model.Session
import io.github.droidkaigi.confsched2018.model.Speaker
import io.github.droidkaigi.confsched2018.util.rx.SchedulerProvider
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.rxkotlin.Flowables
import timber.log.Timber
import javax.inject.Inject

class SessionDataRepository @Inject constructor(
        private val api: DroidKaigiApi,
        private val database: RoomDatabase,
        private val sessionDao: SessionDao,
        private val speakerDao: SpeakerDao,
        private val sessionSpeakerJoinDao: SessionSpeakerJoinDao,
        private val favoriteDatabase: FavoriteDatabase,
        private val schedulerProvider: SchedulerProvider
) : SessionRepository {

    override val rooms: Flowable<List<Room>> =
            sessionDao.getAllRoom().toRooms()
    override val sessions: Flowable<List<Session>> =
            Flowables.combineLatest(
                    sessionSpeakerJoinDao.getAllSessions()
                            .filter { it.isNotEmpty() }
                            .doOnNext { if (DEBUG) Timber.d("getAllSessions") },
                    speakerDao.getAllSpeaker()
                            .filter { it.isNotEmpty() }
                            .doOnNext { if (DEBUG) Timber.d("getAllSpeaker") },
                    Flowable.concat(Flowable.just(listOf()), favoriteDatabase.favorites.onErrorReturn { listOf() })
                            .doOnNext { if (DEBUG) Timber.d("favorites") },
                    { sessionEntities,
                      speakerEntities,
                      favList ->
                        sessionEntities.map { it.toSession(speakerEntities, favList) }
                    })
                    .subscribeOn(schedulerProvider.computation())
                    .doOnNext {
                        if (DEBUG) Timber.d("size:${it.size} current:${System.currentTimeMillis()}")
                    }

    override val speakers: Flowable<List<Speaker>> =
            speakerDao.getAllSpeaker().map { speakers ->
                speakers.map { speaker -> speaker.toSpeaker() }
            }

    override val roomSessions: Flowable<Map<Room, List<Session>>>
            = sessions.map { sessionList -> sessionList.groupBy { it.room } }

    override val topicSessions: Flowable<Map<String, List<Session>>>
            = sessions.map { sessionList -> sessionList.groupBy { it.topic } }

    override val levelSessions: Flowable<Map<String, List<Session>>>
            = sessions.map { sessionList -> sessionList.groupBy { it.topic } }

    override fun favorite(session: Session): Single<Boolean> = favoriteDatabase.favorite(session)

    override fun refreshSessions(): Completable {
        return api.getSessions()
                .doOnSuccess { response ->
                    database.runInTransaction {
                        speakerDao.clearAndInsert(response.speakers.toSpeakerEntities())
                        sessionDao.clearAndInsert(response.sessions.toSessionEntities(response.categories, response.rooms))
                        sessionSpeakerJoinDao.insert(response.sessions.toSessionSpeakerJoinEntities())
                    }
                }
                .subscribeOn(schedulerProvider.computation())
                .toCompletable()
    }

    companion object {
        const val DEBUG = true
    }
}

