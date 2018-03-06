package io.github.droidkaigi.confsched2018.data.repository

import android.support.annotation.CheckResult
import io.github.droidkaigi.confsched2018.model.Session
import io.reactivex.Flowable
import io.reactivex.Single

interface SessionRepository {
    val sessions: Flowable<List<Session>>

    suspend fun refreshSessions()
    @CheckResult fun favorite(session: Session.SpeechSession): Single<Boolean>
}
