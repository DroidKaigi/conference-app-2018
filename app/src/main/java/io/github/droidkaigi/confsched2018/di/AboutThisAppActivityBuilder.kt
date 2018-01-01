package io.github.droidkaigi.confsched2018.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.github.droidkaigi.confsched2018.presentation.about.AboutThisAppActivity

@Module abstract class AboutThisAppActivityBuilder {
    @ContributesAndroidInjector(modules = [
        FragmentBuildersModule::class,
        AboutThisAppActivityModule::class]
    )
    abstract fun contributeAboutThisAppActivity(): AboutThisAppActivity
}
