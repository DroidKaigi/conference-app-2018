package io.github.droidkaigi.confsched2018.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.github.droidkaigi.confsched2018.presentation.map.MapActivity

@Module abstract class MapActivityBuilder {
    @ContributesAndroidInjector(
            modules = [
                FragmentBuildersModule::class,
                MapActivityModule::class
            ]
    )
    abstract fun contributeMapActivity(): MapActivity
}
