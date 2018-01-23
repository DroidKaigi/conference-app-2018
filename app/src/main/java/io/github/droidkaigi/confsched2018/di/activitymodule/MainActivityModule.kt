package io.github.droidkaigi.confsched2018.di.activitymodule

import android.support.v7.app.AppCompatActivity
import dagger.Binds
import dagger.Module
import io.github.droidkaigi.confsched2018.presentation.MainActivity

@Module interface MainActivityModule {
    @Binds fun providesAppCompatActivity(mainActivity: MainActivity): AppCompatActivity
}
