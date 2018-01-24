package io.github.droidkaigi.confsched2018.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.github.droidkaigi.confsched2018.presentation.sessions.feedback.SessionsFeedbackActivity

@Module
interface SessionsFeedbackActivityBuilder {
    @ContributesAndroidInjector(
            modules = [
                FragmentBuildersModule::class,
                SessionsFeedbackActivityModule::class
            ]
    )
    fun contributeSessionsFeedbackActivity(): SessionsFeedbackActivity
}
