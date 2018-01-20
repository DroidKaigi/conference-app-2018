package io.github.droidkaigi.confsched2018.di.activitymodule

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.github.droidkaigi.confsched2018.di.FragmentBuildersModule
import io.github.droidkaigi.confsched2018.presentation.settings.SettingsActivity

@Module interface SettingsActivityBuilder {
    @ContributesAndroidInjector(
            modules = [
                FragmentBuildersModule::class,
                SettingsActivityModule::class
            ]
    )
    fun contributeSettingsActivity(): SettingsActivity
}
