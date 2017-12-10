package io.github.droidkaigi.confsched2018.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider


import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import io.github.droidkaigi.confsched2018.presentation.sessions.AllSessionsViewModel
import io.github.droidkaigi.confsched2018.presentation.sessions.RoomSessionsViewModel
import io.github.droidkaigi.confsched2018.presentation.sessions.SessionsViewModel

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(AllSessionsViewModel::class)
    abstract fun bindAllSessionsViewModel(allSessionsViewModel: AllSessionsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SessionsViewModel::class)
    abstract fun bindSessionsViewModel(sessionsViewModel: SessionsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RoomSessionsViewModel::class)
    abstract fun bindRoomSessionsViewModel(sessionsViewModel: RoomSessionsViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}
