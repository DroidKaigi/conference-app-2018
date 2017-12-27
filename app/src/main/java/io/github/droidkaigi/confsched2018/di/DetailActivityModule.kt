package io.github.droidkaigi.confsched2018.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.github.droidkaigi.confsched2018.presentation.detail.DetailActivity

@Module abstract class DetailActivityModule {
    @ContributesAndroidInjector()
    abstract fun contributeDetailActivity(): DetailActivity
}
