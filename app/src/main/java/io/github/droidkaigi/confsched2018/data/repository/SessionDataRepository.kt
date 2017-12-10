package io.github.droidkaigi.confsched2018.data.repository

import android.util.Log
import io.github.droidkaigi.confsched2018.data.api.DroidKaigiApi
import io.github.droidkaigi.confsched2018.data.api.response.mapper.toSessionEntity
import io.github.droidkaigi.confsched2018.data.api.response.mapper.toSessionSpeakerJoinEntity
import io.github.droidkaigi.confsched2018.data.api.response.mapper.toSpeakerEntity
import io.github.droidkaigi.confsched2018.data.db.dao.SessionDao
import io.github.droidkaigi.confsched2018.data.db.dao.SessionSpeakerJoinDao
import io.github.droidkaigi.confsched2018.data.db.dao.SpeakerDao
import io.github.droidkaigi.confsched2018.data.db.entity.mapper.toRooms
import io.github.droidkaigi.confsched2018.data.db.entity.mapper.toSessions
import io.github.droidkaigi.confsched2018.model.Room
import io.github.droidkaigi.confsched2018.model.Session
import io.github.droidkaigi.confsched2018.util.rx.SchedulerProvider
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Flowable.just
import io.reactivex.rxkotlin.Flowables
import javax.inject.Inject


class SessionDataRepository @Inject constructor(
        private val api: DroidKaigiApi,
        private val sessionDao: SessionDao,
        private val speakerDao: SpeakerDao,
        private val sessionSpeakerJoinDao: SessionSpeakerJoinDao,
        private val schedulerProvider: SchedulerProvider
) : SessionRepository {
    override val rooms: Flowable<List<Room>> =
            sessionDao.getAllRoom().toRooms()
    override val sessions: Flowable<List<Session>> =
            Flowables.combineLatest(sessionSpeakerJoinDao.getAllSessions(),
                    speakerDao.getAllSpeaker(),
                    just(listOf<Int>()),
                    { sessionEntities, speakerEntities, favList ->
                        sessionEntities.toSessions(speakerEntities, favList)
                    })
                    .subscribeOn(schedulerProvider.computation())
                    .doOnNext {
                        Log.d("SessionDataRepository", "size:" + it.size + " current:" + System.currentTimeMillis())
                    }
    override val roomSessions: Flowable<Map<Room, List<Session>>>
            = sessions.map { sessionList -> sessionList.groupBy { it.room } }


    override

    fun refreshSessions(): Completable {
        return api.getSessions()
                .doOnSuccess { response ->
                    speakerDao.clearAndInsert(response.speakers.toSpeakerEntity())
                    sessionDao.clearAndInsert(response.sessions.toSessionEntity(response.categories, response.rooms))
                    sessionSpeakerJoinDao.insert(response.sessions.toSessionSpeakerJoinEntity())
                }
                .subscribeOn(schedulerProvider.computation())
                .toCompletable()
    }
}



