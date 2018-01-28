package io.github.droidkaigi.confsched2018.di.activitymodule

import android.arch.lifecycle.ViewModel
import android.support.v7.app.AppCompatActivity
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import io.github.droidkaigi.confsched2018.di.ViewModelKey
import io.github.droidkaigi.confsched2018.presentation.contributor.ContributorsActivity
import io.github.droidkaigi.confsched2018.presentation.contributor.ContributorsFragment
import io.github.droidkaigi.confsched2018.presentation.contributor.ContributorsViewModel

@Module interface ContributorsActivityModule {
    @Binds fun providesAppCompatActivity(activity: ContributorsActivity): AppCompatActivity

    @ContributesAndroidInjector fun contributeContributorFragment(): ContributorsFragment

    @Binds @IntoMap
    @ViewModelKey(ContributorsViewModel::class)
    fun bindContributorsViewModel(
            contributorsViewModel: ContributorsViewModel
    ): ViewModel
}
