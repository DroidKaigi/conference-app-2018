package io.github.droidkaigi.confsched2018.di.activitymodule

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.github.droidkaigi.confsched2018.presentation.detail.SessionDetailActivity

@Module interface SessionDetailActivityBuilder {
    @ContributesAndroidInjector(modules = [SessionDetailActivityModule::class])
    fun contributeSessionDetailActivity(): SessionDetailActivity
}
