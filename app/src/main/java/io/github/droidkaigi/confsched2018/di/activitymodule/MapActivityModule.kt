package io.github.droidkaigi.confsched2018.di.activitymodule

import android.support.v7.app.AppCompatActivity
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.github.droidkaigi.confsched2018.presentation.map.MapActivity
import io.github.droidkaigi.confsched2018.presentation.map.MapFragment

@Module interface MapActivityModule {
    @Binds fun providesAppCompatActivity(activity: MapActivity): AppCompatActivity

    @ContributesAndroidInjector fun contributeMapFragment(): MapFragment
}
