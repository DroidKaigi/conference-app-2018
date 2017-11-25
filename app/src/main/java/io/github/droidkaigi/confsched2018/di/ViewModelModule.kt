package io.github.droidkaigi.confsched2018.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider


import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import io.github.droidkaigi.confsched2018.presentation.ViewModelFactory
import io.github.droidkaigi.confsched2018.presentation.sessions.AllSessionsViewModel

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(AllSessionsViewModel::class)
    abstract fun bindUserViewModel(userViewModel: AllSessionsViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}
