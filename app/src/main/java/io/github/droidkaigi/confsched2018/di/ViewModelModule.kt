package io.github.droidkaigi.confsched2018.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import io.github.droidkaigi.confsched2018.presentation.feed.FeedViewModel
import io.github.droidkaigi.confsched2018.presentation.search.SearchViewModel
import io.github.droidkaigi.confsched2018.presentation.sessions.AllSessionsViewModel
import io.github.droidkaigi.confsched2018.presentation.sessions.FavoriteSessionsViewModel
import io.github.droidkaigi.confsched2018.presentation.sessions.RoomSessionsViewModel
import io.github.droidkaigi.confsched2018.presentation.sessions.SessionsViewModel

@Module abstract class ViewModelModule {
    @Binds @IntoMap
    @ViewModelKey(AllSessionsViewModel::class)
    abstract fun bindAllSessionsViewModel(allSessionsViewModel: AllSessionsViewModel): ViewModel

    @Binds @IntoMap
    @ViewModelKey(FavoriteSessionsViewModel::class)
    abstract fun bindFavoriteSessionsViewModel(allSessionsViewModel: FavoriteSessionsViewModel): ViewModel

    @Binds @IntoMap
    @ViewModelKey(SessionsViewModel::class)
    abstract fun bindSessionsViewModel(sessionsViewModel: SessionsViewModel): ViewModel

    @Binds @IntoMap
    @ViewModelKey(RoomSessionsViewModel::class)
    abstract fun bindRoomSessionsViewModel(sessionsViewModel: RoomSessionsViewModel): ViewModel

    @Binds @IntoMap
    @ViewModelKey(SearchViewModel::class)
    abstract fun bindSearchViewModel(searchViewModel: SearchViewModel): ViewModel

    @Binds @IntoMap
    @ViewModelKey(FeedViewModel::class)
    abstract fun bindFeedViewModel(searchViewModel: FeedViewModel): ViewModel


    @Binds abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}
