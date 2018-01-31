package io.github.droidkaigi.confsched2018.di.activitymodule

import android.support.v7.app.AppCompatActivity
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.github.droidkaigi.confsched2018.presentation.about.AboutThisAppActivity
import io.github.droidkaigi.confsched2018.presentation.about.AboutThisAppFragment

@Module interface AboutThisAppActivityModule {
    @Binds fun providesAppCompatActivity(activity: AboutThisAppActivity): AppCompatActivity

    @ContributesAndroidInjector fun contributeAboutThisAppFragment(): AboutThisAppFragment
}
