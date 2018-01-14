package io.github.droidkaigi.confsched2018.presentation.speaker

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import io.github.droidkaigi.confsched2018.data.repository.SessionRepository
import io.github.droidkaigi.confsched2018.model.Session
import io.github.droidkaigi.confsched2018.model.Speaker
import io.github.droidkaigi.confsched2018.presentation.Result
import io.github.droidkaigi.confsched2018.presentation.common.mapper.toResult
import io.github.droidkaigi.confsched2018.util.defaultErrorHandler
import io.github.droidkaigi.confsched2018.util.ext.toLiveData
import io.github.droidkaigi.confsched2018.util.rx.SchedulerProvider
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class SpeakerDetailViewModel @Inject constructor(
        private val repository: SessionRepository,
        private val schedulerProvider: SchedulerProvider
) : ViewModel() {
    lateinit var speakerId: String
    val speakerSessions: LiveData<Result<Pair<Speaker, List<Session.SpeechSession>>>> by lazy {
        repository
                .speakerSessions
                .map {
                    it
                            .filter { it.key.id == speakerId }
                            .map { it.key to it.value }
                            .first()
                }
                .toResult(schedulerProvider)
                .toLiveData()
    }
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    fun onFavoriteClick(session: Session.SpeechSession) {
        val favoriteSingle: Single<Boolean> = repository.favorite(session)
        favoriteSingle
                .subscribeBy(onError = defaultErrorHandler())
                .addTo(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}
