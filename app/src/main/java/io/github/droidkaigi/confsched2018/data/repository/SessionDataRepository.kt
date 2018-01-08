package io.github.droidkaigi.confsched2018.data.repository

import io.github.droidkaigi.confsched2018.data.api.DroidKaigiApi
import io.github.droidkaigi.confsched2018.data.api.response.mapper.LocalDateTimeAdapter
import io.github.droidkaigi.confsched2018.data.db.FavoriteDatabase
import io.github.droidkaigi.confsched2018.data.db.SessionDatabase
import io.github.droidkaigi.confsched2018.data.db.entity.mapper.Converters
import io.github.droidkaigi.confsched2018.data.db.entity.mapper.toRooms
import io.github.droidkaigi.confsched2018.data.db.entity.mapper.toSession
import io.github.droidkaigi.confsched2018.data.db.entity.mapper.toSpeaker
import io.github.droidkaigi.confsched2018.data.db.entity.mapper.toTopics
import io.github.droidkaigi.confsched2018.model.Level
import io.github.droidkaigi.confsched2018.model.Room
import io.github.droidkaigi.confsched2018.model.SearchResult
import io.github.droidkaigi.confsched2018.model.Session
import io.github.droidkaigi.confsched2018.model.Speaker
import io.github.droidkaigi.confsched2018.model.Topic
import io.github.droidkaigi.confsched2018.model.parseDate
import io.github.droidkaigi.confsched2018.util.ext.toUnixMills
import io.github.droidkaigi.confsched2018.util.rx.SchedulerProvider
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.rxkotlin.Flowables
import io.reactivex.rxkotlin.Singles
import timber.log.Timber
import javax.inject.Inject

class SessionDataRepository @Inject constructor(
        private val api: DroidKaigiApi,
        private val sessionDatabase: SessionDatabase,
        private val favoriteDatabase: FavoriteDatabase,
        private val schedulerProvider: SchedulerProvider
) : SessionRepository {

    override val rooms: Flowable<List<Room>> =
            sessionDatabase.getAllRoom().toRooms()
    override val topics: Flowable<List<Topic>> =
            sessionDatabase.getAllTopic().toTopics()
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
                        val firstDay = sessionEntities.first().session!!.stime.toLocalDate()
                        sessionEntities
                                .map { it.toSession(speakerEntities, favList, firstDay) } +
                                specialSessions()
                    })
                    .subscribeOn(schedulerProvider.computation())
                    .doOnNext {
                        if (DEBUG) Timber.d("size:${it.size} current:${System.currentTimeMillis()}")
                    }

    override val speakers: Flowable<List<Speaker>> =
            sessionDatabase.getAllSpeaker()
                    .map { speakers ->
                        speakers.map { speaker -> speaker.toSpeaker() }
                    }

    override val roomSessions: Flowable<Map<Room, List<Session.SpeechSession>>> =
            sessions.map { sessionList ->
                sessionList
                        .filterIsInstance<Session.SpeechSession>()
                        .groupBy { it.room }
            }

    override val topicSessions: Flowable<Map<Topic, List<Session.SpeechSession>>> =
            sessions.map { sessionList ->
                sessionList
                        .filterIsInstance<Session.SpeechSession>()
                        .groupBy { it.topic }
            }

    override val speakerSessions: Flowable<Map<Speaker, List<Session.SpeechSession>>> =
            sessions.map { sessionList ->
                sessionList
                        .filterIsInstance<Session.SpeechSession>()
                        .flatMap { session ->
                            session.speakers.map {
                                it to session
                            }
                        }
                        .groupBy({ it.first }, { it.second })
            }

    override val levelSessions: Flowable<Map<Level, List<Session.SpeechSession>>> =
            sessions.map { sessionList ->
                sessionList
                        .filterIsInstance<Session.SpeechSession>()
                        .groupBy { it.level }
            }

    override fun favorite(session: Session.SpeechSession): Single<Boolean> = favoriteDatabase.favorite(session)

    override fun refreshSessions(): Completable {
        return api.getSessions()
                .doOnSuccess { response ->
                    sessionDatabase.save(response)
                }
                .subscribeOn(schedulerProvider.computation())
                .toCompletable()
    }

    override fun search(query: String): Single<SearchResult> = Singles.zip(
            sessions.map {
                it
                        .filterIsInstance<Session.SpeechSession>()
                        .filter { it.title.contains(query) || it.desc.contains(query) }
            }.firstOrError(),
            speakers.map {
                it.filter { it.name.contains(query) }
            }.firstOrError(),
            { sessions: List<Session>, speakers: List<Speaker> ->
                SearchResult(sessions, speakers)
            })

    fun specialSessions(): List<Session.SpecialSession> {
        var index = 0
        return listOf(
                Session.SpecialSession(
                        "100000" + index++,
                        "welcomeTalk",
                        1,
                        parseDate(
                                LocalDateTimeAdapter
                                        .parseDateString("2018-02-08T10:00:00")
                                        .toUnixMills()
                        ),
                        parseDate(
                                LocalDateTimeAdapter
                                        .parseDateString("2018-02-08T10:20:00")
                                        .toUnixMills()
                        ),
                        Room(513, "Hall")
                ),
                Session.SpecialSession(
                        "100000" + index++,
                        "lunch",
                        1,
                        parseDate(
                                LocalDateTimeAdapter.parseDateString("2018-02-08T11:50:00")
                                        .toUnixMills()
                        ),
                        parseDate(
                                LocalDateTimeAdapter.parseDateString("2018-02-08T10:20:00")
                                        .toUnixMills()
                        ),
                        Room(513, "Hall")
                ),
                Session.SpecialSession(
                        "100000" + index++,
                        "party",
                        1,
                        parseDate(
                                LocalDateTimeAdapter.parseDateString("2018-02-08T17:40:00")
                                        .toUnixMills()
                        ),
                        parseDate(
                                LocalDateTimeAdapter.parseDateString("2018-02-08T19:40:00")
                                        .toUnixMills()
                        ),
                        Room(513, "Hall")
                ),

                Session.SpecialSession(
                        "100000" + index++,
                        "lunch",
                        2,
                        parseDate(
                                LocalDateTimeAdapter.parseDateString("2018-02-09T11:50:00")
                                        .toUnixMills()
                        ),
                        parseDate(
                                LocalDateTimeAdapter.parseDateString("2018-02-09T12:50:00")
                                        .toUnixMills()
                        ),
                        Room(513, "Hall")
                ))
    }

    companion object {
        const val DEBUG = false
    }
}
