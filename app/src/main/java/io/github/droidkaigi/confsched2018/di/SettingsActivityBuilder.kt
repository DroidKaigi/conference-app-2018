package io.github.droidkaigi.confsched2018.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.github.droidkaigi.confsched2018.presentation.detail.SessionDetailActivity
import io.github.droidkaigi.confsched2018.presentation.settings.SettingsActivity

@Module abstract class SettingsActivityBuilder {
    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class, SettingsActivityModule::class])
    abstract fun contributeSettingsActivity(): SettingsActivity

}
