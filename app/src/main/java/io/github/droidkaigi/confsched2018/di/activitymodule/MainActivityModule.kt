package io.github.droidkaigi.confsched2018.di.activitymodule

import android.arch.lifecycle.ViewModel
import android.support.v7.app.AppCompatActivity
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import io.github.droidkaigi.confsched2018.di.ViewModelKey
import io.github.droidkaigi.confsched2018.presentation.MainActivity
import io.github.droidkaigi.confsched2018.presentation.sessions.AllSessionsFragment
import io.github.droidkaigi.confsched2018.presentation.sessions.AllSessionsStore

@Module interface MainActivityModule {
    @Binds fun providesAppCompatActivity(mainActivity: MainActivity): AppCompatActivity

    @ContributesAndroidInjector fun contributeAllSessionsFragment(): AllSessionsFragment

    @Binds @IntoMap
    @ViewModelKey(AllSessionsStore::class)
    fun bindAllSessionsStore(
            allSessionsStore: AllSessionsStore
    ): ViewModel
}
