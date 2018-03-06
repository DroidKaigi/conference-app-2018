package io.github.droidkaigi.confsched2018.presentation.sessions

import io.github.droidkaigi.confsched2018.data.repository.SessionRepository
import io.github.droidkaigi.confsched2018.model.LoadState
import io.github.droidkaigi.confsched2018.model.Session
import io.github.droidkaigi.confsched2018.presentation.action.SessionRefreshLoadStateChangeAction
import io.github.droidkaigi.confsched2018.presentation.dispacher.Dispatcher
import io.github.droidkaigi.confsched2018.util.defaultErrorHandler
import io.reactivex.Single
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import javax.inject.Inject

class AllSessionsActionCreator @Inject constructor(
        private val dispatcher: Dispatcher,
        private val repository: SessionRepository
) {

    fun refreshSessions() {
        launch(UI) {
            try {
                dispatcher.send(SessionRefreshLoadStateChangeAction(LoadState.Loading))
                repository.refreshSessions()
                dispatcher.send(SessionRefreshLoadStateChangeAction(LoadState.Finished))
            } catch (e: Exception) {
                dispatcher.send(
                        SessionRefreshLoadStateChangeAction(LoadState.Error(e))
                )
            }
        }
    }

    fun favorite(session: Session.SpeechSession) {
        val favoriteSingle: Single<Boolean> = repository.favorite(session)
        favoriteSingle
                .subscribeBy(onError = defaultErrorHandler())
    }
}
