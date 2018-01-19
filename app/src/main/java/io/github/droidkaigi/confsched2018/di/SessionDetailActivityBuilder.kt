package io.github.droidkaigi.confsched2018.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.github.droidkaigi.confsched2018.presentation.detail.SessionDetailActivity

@Module interface SessionDetailActivityBuilder {
    @ContributesAndroidInjector(
            modules = [
                FragmentBuildersModule::class,
                SessionDetailActivityModule::class
            ]
    )
    fun contributeSessionDetailActivity(): SessionDetailActivity
}
