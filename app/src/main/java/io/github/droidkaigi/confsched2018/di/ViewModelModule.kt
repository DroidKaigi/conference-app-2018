package io.github.droidkaigi.confsched2018.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import io.github.droidkaigi.confsched2018.presentation.detail.SessionDetailViewModel
import io.github.droidkaigi.confsched2018.presentation.favorite.FavoriteSessionsViewModel
import io.github.droidkaigi.confsched2018.presentation.feed.FeedViewModel
import io.github.droidkaigi.confsched2018.presentation.search.SearchSessionViewModel
import io.github.droidkaigi.confsched2018.presentation.search.SearchViewModel
import io.github.droidkaigi.confsched2018.presentation.sessions.AllSessionsViewModel
import io.github.droidkaigi.confsched2018.presentation.sessions.RoomSessionsViewModel
import io.github.droidkaigi.confsched2018.presentation.sessions.SessionsViewModel

@Module abstract class ViewModelModule {
    @Binds @IntoMap
    @ViewModelKey(AllSessionsViewModel::class)
    abstract fun bindAllSessionsViewModel(allSessionsViewModel: AllSessionsViewModel): ViewModel

    @Binds @IntoMap
    @ViewModelKey(FavoriteSessionsViewModel::class)
    abstract fun bindFavoriteSessionsViewModel(favoriteSessionsViewModel: FavoriteSessionsViewModel): ViewModel

    @Binds @IntoMap
    @ViewModelKey(SessionsViewModel::class)
    abstract fun bindSessionsViewModel(sessionsViewModel: SessionsViewModel): ViewModel

    @Binds @IntoMap
    @ViewModelKey(RoomSessionsViewModel::class)
    abstract fun bindRoomSessionsViewModel(roomSessionsViewModel: RoomSessionsViewModel): ViewModel

    @Binds @IntoMap
    @ViewModelKey(SearchSessionViewModel::class)
    abstract fun bindSearchSessionViewModel(searchSessionViewModel: SearchSessionViewModel): ViewModel


    @Binds @IntoMap
    @ViewModelKey(SearchViewModel::class)
    abstract fun bindSearchViewModel(searchViewModel: SearchViewModel): ViewModel

    @Binds @IntoMap
    @ViewModelKey(FeedViewModel::class)
    abstract fun bindFeedViewModel(feedViewModel: FeedViewModel): ViewModel

    @Binds @IntoMap
    @ViewModelKey(SessionDetailViewModel::class)
    abstract fun bindSessionDetailViewModel(sessionDetailViewModel: SessionDetailViewModel): ViewModel

    @Binds abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}
