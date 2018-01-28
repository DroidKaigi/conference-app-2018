package io.github.droidkaigi.confsched2018.presentation

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.cantrowitz.rxbroadcast.RxBroadcast
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.presentation.common.pref.Prefs
import io.github.droidkaigi.confsched2018.util.ext.asFlowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class MainViewModel @Inject constructor(context: Context) : ViewModel() {

    private val mutableLocalTimeConfig: MutableLiveData<Boolean> = MutableLiveData()
    val localTimeConfig: LiveData<Boolean> = mutableLocalTimeConfig
    private val mutableBottomNavigationBarConfig: MutableLiveData<Boolean> = MutableLiveData()
    val bottomNavigationBarConfig: LiveData<Boolean> = mutableBottomNavigationBarConfig
    private val mutableLastTimeZoneChangeIntent: MutableLiveData<Intent> = MutableLiveData()
    val lastTimeZoneChangeIntent: LiveData<Intent> = mutableLastTimeZoneChangeIntent
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    init {
        Prefs
                .asFlowable(
                        Prefs::enableHideBottomNavigationBar,
                        R.string.pref_key_enable_hide_bottom_navigation
                ).subscribeBy(
                onNext = { mutableBottomNavigationBarConfig.value = it }
        )
                .addTo(compositeDisposable)
        Prefs
                .asFlowable(
                        Prefs::enableLocalTime,
                        R.string.pref_key_enable_local_time
                ).subscribeBy(
                onNext = { mutableLocalTimeConfig.value = it }
        )
                .addTo(compositeDisposable)
        RxBroadcast.fromBroadcast(context, IntentFilter(Intent.ACTION_TIMEZONE_CHANGED))
                .subscribeBy(
                        onNext = { mutableLastTimeZoneChangeIntent.value = it }
                )
                .addTo(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}
