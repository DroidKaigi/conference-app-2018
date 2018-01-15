package io.github.droidkaigi.confsched2018.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.github.droidkaigi.confsched2018.presentation.contributor.ContributorsActivity

@Module abstract class ContributorActivityBuilder {
    @ContributesAndroidInjector(modules = [
        FragmentBuildersModule::class,
        ContributorModule::class]
    )
    abstract fun contributeContributorActivity(): ContributorsActivity
}
