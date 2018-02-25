package io.github.droidkaigi.confsched2018.presentation.sessions

import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import io.github.droidkaigi.confsched2018.model.LoadState
import io.github.droidkaigi.confsched2018.presentation.action.SessionRefreshLoadStateChangeAction
import io.github.droidkaigi.confsched2018.presentation.dispacher.Dispatcher
import io.github.droidkaigi.confsched2018.util.ext.toLiveData
import javax.inject.Inject

class AllSessionsStore @Inject constructor(
        dispatcher: Dispatcher
) : ViewModel(), LifecycleObserver {
    val refreshLoadState: LiveData<LoadState> by lazy {
        dispatcher
                .asFlowable<SessionRefreshLoadStateChangeAction>()
                .map { it.loadState }
                .toLiveData()
    }
}
