package io.github.droidkaigi.confsched2018.presentation

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.OnLifecycleEvent
import android.arch.lifecycle.ViewModel
import android.content.Intent
import io.github.droidkaigi.confsched2018.presentation.common.pref.PrefsKeyChangedListener
import io.github.droidkaigi.confsched2018.presentation.common.pref.TimeZoneChangedListener
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class MainViewModel @Inject constructor(
        private val prefsKeyChangedListener: PrefsKeyChangedListener,
        private val timeZoneChangedListener: TimeZoneChangedListener
) : ViewModel(), LifecycleObserver {

    val configChangeEvent: MutableLiveData<String> = MutableLiveData()
    val timeZoneChangedEvent: MutableLiveData<Intent> = MutableLiveData()
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        prefsKeyChangedListener.listener
                .subscribeBy(
                        onNext = { configChangeEvent.value = it }
                )
                .addTo(compositeDisposable)
        timeZoneChangedListener.listener
                .subscribeBy(
                        onNext = { timeZoneChangedEvent.value = it }
                )
                .addTo(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}
