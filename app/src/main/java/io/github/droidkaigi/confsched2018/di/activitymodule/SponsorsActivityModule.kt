package io.github.droidkaigi.confsched2018.di.activitymodule

import android.arch.lifecycle.ViewModel
import android.support.v7.app.AppCompatActivity
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import io.github.droidkaigi.confsched2018.di.ViewModelKey
import io.github.droidkaigi.confsched2018.presentation.sponsors.SponsorsActivity
import io.github.droidkaigi.confsched2018.presentation.sponsors.SponsorsFragment
import io.github.droidkaigi.confsched2018.presentation.sponsors.SponsorsViewModel

@Module interface SponsorsActivityModule {
    @Binds fun providesAppCompatActivity(activity: SponsorsActivity): AppCompatActivity

    @ContributesAndroidInjector fun contributeSponsorsFragment(): SponsorsFragment

    @Binds @IntoMap
    @ViewModelKey(SponsorsViewModel::class)
    fun bindSponsorsViewModel(
            sponsorsViewModel: SponsorsViewModel
    ): ViewModel
}
