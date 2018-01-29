package io.github.droidkaigi.confsched2018.di.activitymodule

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.github.droidkaigi.confsched2018.presentation.sessions.feedback.SessionsFeedbackActivity

@Module
interface SessionsFeedbackActivityBuilder {
    @ContributesAndroidInjector(modules = [SessionsFeedbackActivityModule::class])
    fun contributeSessionsFeedbackActivity(): SessionsFeedbackActivity
}
