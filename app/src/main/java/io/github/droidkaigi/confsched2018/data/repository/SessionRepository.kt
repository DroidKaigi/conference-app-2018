package io.github.droidkaigi.confsched2018.data.repository

import android.support.annotation.CheckResult
import io.github.droidkaigi.confsched2018.model.Level
import io.github.droidkaigi.confsched2018.model.Room
import io.github.droidkaigi.confsched2018.model.SearchResult
import io.github.droidkaigi.confsched2018.model.Session
import io.github.droidkaigi.confsched2018.model.SessionFeedback
import io.github.droidkaigi.confsched2018.model.SessionSchedule
import io.github.droidkaigi.confsched2018.model.Speaker
import io.github.droidkaigi.confsched2018.model.Topic
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

interface SessionRepository {
    val sessions: Flowable<List<Session>>
    val speakers: Flowable<List<Speaker>>
    val roomSessions: Flowable<Map<Room, List<Session>>>
    val scheduleSessions: Flowable<Map<SessionSchedule, List<Session>>>
    val rooms: Flowable<List<Room>>
    val schedules: Flowable<List<SessionSchedule>>
    val topics: Flowable<List<Topic>>
    val speakerSessions: Flowable<Map<Speaker, List<Session.SpeechSession>>>
    val topicSessions: Flowable<Map<Topic, List<Session.SpeechSession>>>
    val levelSessions: Flowable<Map<Level, List<Session.SpeechSession>>>

    @CheckResult fun refreshSessions(): Completable
    @CheckResult fun favorite(session: Session.SpeechSession): Single<Boolean>
    @CheckResult fun search(query: String): Single<SearchResult>
    @CheckResult fun saveSessionFeedback(sessionFeedback: SessionFeedback): Completable
    @CheckResult fun submitSessionFeedback(
            session: Session.SpeechSession,
            sessionFeedback: SessionFeedback
    ): Completable
}
