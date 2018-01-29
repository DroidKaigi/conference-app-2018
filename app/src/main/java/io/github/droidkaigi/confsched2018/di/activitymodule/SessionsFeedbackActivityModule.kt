package io.github.droidkaigi.confsched2018.di.activitymodule

import android.arch.lifecycle.ViewModel
import android.support.v7.app.AppCompatActivity
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import io.github.droidkaigi.confsched2018.di.ViewModelKey
import io.github.droidkaigi.confsched2018.presentation.sessions.feedback.SessionsFeedbackActivity
import io.github.droidkaigi.confsched2018.presentation.sessions.feedback.SessionsFeedbackFragment
import io.github.droidkaigi.confsched2018.presentation.sessions.feedback.SessionsFeedbackViewModel

@Module
interface SessionsFeedbackActivityModule {
    @Binds fun providesAppCompatActivity(activity: SessionsFeedbackActivity): AppCompatActivity

    @ContributesAndroidInjector fun contributeSessionsFeedbackFragment(): SessionsFeedbackFragment

    @Binds @IntoMap
    @ViewModelKey(SessionsFeedbackViewModel::class)
    fun bindSessionsFeedbackViewModel(
            sessionsFeedbackViewModel: SessionsFeedbackViewModel
    ): ViewModel
}
