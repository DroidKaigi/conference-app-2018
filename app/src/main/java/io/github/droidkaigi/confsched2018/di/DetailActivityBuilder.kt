package io.github.droidkaigi.confsched2018.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.github.droidkaigi.confsched2018.presentation.detail.DetailActivity

@Module abstract class DetailActivityBuilder {
    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class, DetailActivityModule::class])
    abstract fun contributeDetailActivity(): DetailActivity

}
