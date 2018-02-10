package io.github.droidkaigi.confsched2018.di.activitymodule

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.github.droidkaigi.confsched2018.di.RecycledViewPoolModule
import io.github.droidkaigi.confsched2018.presentation.MainActivity

@Module interface MainActivityBuilder {
    @ContributesAndroidInjector(modules = [
        MainActivityModule::class,
        RecycledViewPoolModule::class
    ])
    fun contributeMainActivity(): MainActivity
}
