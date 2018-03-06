package io.github.droidkaigi.confsched2018.data.repository

import android.support.annotation.CheckResult
import io.github.droidkaigi.confsched2018.model.Session
import io.reactivex.Flowable
import io.reactivex.Single
import kotlinx.coroutines.experimental.Deferred

interface SessionRepository {
    val sessions: Flowable<List<Session>>

    @CheckResult suspend fun refreshSessions(): Deferred<Deferred<Unit>>
    @CheckResult fun favorite(session: Session.SpeechSession): Single<Boolean>
}
