package io.github.droidkaigi.confsched2018.di.activitymodule

import android.support.v7.app.AppCompatActivity
import dagger.Binds
import dagger.Module
import io.github.droidkaigi.confsched2018.presentation.settings.SettingsActivity

@Module interface SettingsActivityModule {
    @Binds fun providesAppCompatActivity(activity: SettingsActivity): AppCompatActivity
}
