package io.github.droidkaigi.confsched2018.presentation.sessions

import io.github.droidkaigi.confsched2018.data.repository.SessionRepository
import io.github.droidkaigi.confsched2018.model.LoadState
import io.github.droidkaigi.confsched2018.presentation.action.SessionChangedAction
import io.github.droidkaigi.confsched2018.presentation.action.SessionLoadStateChangeAction
import io.github.droidkaigi.confsched2018.presentation.dispacher.Dispatcher
import io.github.droidkaigi.confsched2018.util.defaultErrorHandler
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionsActionCreator @Inject constructor(
        private val dispatcher: Dispatcher,
        private val repository: SessionRepository
) {

    fun subscribeSessionChange() {
        repository.sessions
                .doOnSubscribe {
                    dispatcher.send(SessionLoadStateChangeAction(LoadState.Loading))
                }
                .subscribeBy(
                        onNext = {
                            dispatcher.send(SessionLoadStateChangeAction(LoadState.Finished))
                            dispatcher.send(SessionChangedAction(it)
                            )
                        },
                        onError = defaultErrorHandler()
                )
    }

}
