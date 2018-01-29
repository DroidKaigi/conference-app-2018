package io.github.droidkaigi.confsched2018.di.activitymodule

import android.support.v7.app.AppCompatActivity
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.github.droidkaigi.confsched2018.presentation.settings.SettingsActivity
import io.github.droidkaigi.confsched2018.presentation.settings.SettingsFragment

@Module interface SettingsActivityModule {
    @Binds fun providesAppCompatActivity(activity: SettingsActivity): AppCompatActivity

    @ContributesAndroidInjector fun contributeSettingsFragment(): SettingsFragment
}
