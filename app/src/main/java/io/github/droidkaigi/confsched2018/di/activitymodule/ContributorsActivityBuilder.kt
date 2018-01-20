package io.github.droidkaigi.confsched2018.di.activitymodule

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.github.droidkaigi.confsched2018.di.FragmentBuildersModule
import io.github.droidkaigi.confsched2018.presentation.contributor.ContributorsActivity

@Module interface ContributorsActivityBuilder {
    @ContributesAndroidInjector(modules = [
        FragmentBuildersModule::class,
        ContributorsActivityModule::class]
    )
    fun contributeContributorActivity(): ContributorsActivity
}
