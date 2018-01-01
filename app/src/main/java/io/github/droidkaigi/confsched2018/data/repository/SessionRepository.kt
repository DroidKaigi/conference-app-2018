package io.github.droidkaigi.confsched2018.data.repository

import android.support.annotation.CheckResult
import io.github.droidkaigi.confsched2018.model.*
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

interface SessionRepository {
    val sessions: Flowable<List<Session>>
    val speakers: Flowable<List<Speaker>>
    val roomSessions: Flowable<Map<Room, List<Session>>>
    val rooms: Flowable<List<Room>>
    val speakerSessions: Flowable<Map<Speaker, List<Session>>>
    val topicSessions: Flowable<Map<Topic, List<Session>>>
    val levelSessions: Flowable<Map<Level, List<Session>>>

    @CheckResult fun refreshSessions(): Completable
    @CheckResult fun favorite(session: Session): Single<Boolean>
    @CheckResult fun search(query: String): Single<SearchResult>

}
