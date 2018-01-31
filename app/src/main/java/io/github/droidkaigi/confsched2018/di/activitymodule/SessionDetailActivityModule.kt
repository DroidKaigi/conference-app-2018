package io.github.droidkaigi.confsched2018.di.activitymodule

import android.arch.lifecycle.ViewModel
import android.support.v7.app.AppCompatActivity
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import io.github.droidkaigi.confsched2018.di.ViewModelKey
import io.github.droidkaigi.confsched2018.presentation.detail.SessionDetailActivity
import io.github.droidkaigi.confsched2018.presentation.detail.SessionDetailFragment
import io.github.droidkaigi.confsched2018.presentation.detail.SessionDetailViewModel

@Module interface SessionDetailActivityModule {
    @Binds fun providesAppCompatActivity(activity: SessionDetailActivity): AppCompatActivity

    @ContributesAndroidInjector fun contributeDetailFragment(): SessionDetailFragment

    @Binds @IntoMap
    @ViewModelKey(SessionDetailViewModel::class)
    fun bindSessionDetailViewModel(
            sessionDetailViewModel: SessionDetailViewModel
    ): ViewModel
}
