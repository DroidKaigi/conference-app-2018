package io.github.droidkaigi.confsched2018.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.github.droidkaigi.confsched2018.presentation.MainActivity

@Module abstract class MainActivityBuilder {
    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class, MainActivityModule::class])
    abstract fun contributeMainActivity(): MainActivity
}
