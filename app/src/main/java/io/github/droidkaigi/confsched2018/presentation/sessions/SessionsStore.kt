package io.github.droidkaigi.confsched2018.presentation.sessions

import android.arch.lifecycle.LiveData
import io.github.droidkaigi.confsched2018.model.LoadState
import io.github.droidkaigi.confsched2018.model.Session
import io.github.droidkaigi.confsched2018.presentation.action.SessionChangedAction
import io.github.droidkaigi.confsched2018.presentation.action.SessionLoadStateChangeAction
import io.github.droidkaigi.confsched2018.presentation.dispacher.Dispatcher
import io.github.droidkaigi.confsched2018.util.ext.toLiveData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionsStore @Inject constructor(
        private val dispatcher: Dispatcher
) {
    val sessions: LiveData<List<Session>> by lazy {
        dispatcher.asFlowable<SessionChangedAction>()
                .map { it.sessions }
                .toLiveData()
    }

    val loadingState: LiveData<LoadState> by lazy {
        dispatcher.asFlowable<SessionLoadStateChangeAction>()
                .map { it.loadState }
                .toLiveData()
    }
}
