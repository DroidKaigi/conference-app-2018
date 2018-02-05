package io.github.droidkaigi.confsched2018.di.activitymodule

import android.arch.lifecycle.ViewModel
import android.support.v7.app.AppCompatActivity
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import io.github.droidkaigi.confsched2018.di.ViewModelKey
import io.github.droidkaigi.confsched2018.presentation.MainActivity
import io.github.droidkaigi.confsched2018.presentation.MainViewModel
import io.github.droidkaigi.confsched2018.presentation.favorite.FavoriteSessionsFragment
import io.github.droidkaigi.confsched2018.presentation.favorite.FavoriteSessionsViewModel
import io.github.droidkaigi.confsched2018.presentation.feed.FeedFragment
import io.github.droidkaigi.confsched2018.presentation.feed.FeedViewModel
import io.github.droidkaigi.confsched2018.presentation.search.SearchFragment
import io.github.droidkaigi.confsched2018.presentation.search.SearchSessionsFragment
import io.github.droidkaigi.confsched2018.presentation.search.SearchSessionsViewModel
import io.github.droidkaigi.confsched2018.presentation.search.SearchSpeakersFragment
import io.github.droidkaigi.confsched2018.presentation.search.SearchSpeakersViewModel
import io.github.droidkaigi.confsched2018.presentation.search.SearchTopicsFragment
import io.github.droidkaigi.confsched2018.presentation.search.SearchTopicsViewModel
import io.github.droidkaigi.confsched2018.presentation.search.SearchViewModel
import io.github.droidkaigi.confsched2018.presentation.sessions.AllSessionsFragment
import io.github.droidkaigi.confsched2018.presentation.sessions.AllSessionsViewModel
import io.github.droidkaigi.confsched2018.presentation.sessions.RoomSessionsFragment
import io.github.droidkaigi.confsched2018.presentation.sessions.RoomSessionsViewModel
import io.github.droidkaigi.confsched2018.presentation.sessions.ScheduleSessionsFragment
import io.github.droidkaigi.confsched2018.presentation.sessions.ScheduleSessionsViewModel
import io.github.droidkaigi.confsched2018.presentation.sessions.SessionsFragment
import io.github.droidkaigi.confsched2018.presentation.sessions.SessionsViewModel

@Module interface MainActivityModule {
    @Binds fun providesAppCompatActivity(mainActivity: MainActivity): AppCompatActivity

    @ContributesAndroidInjector fun contributeAllSessionsFragment(): AllSessionsFragment

    @ContributesAndroidInjector fun contributeFavoriteSessionsFragment(): FavoriteSessionsFragment

    @ContributesAndroidInjector fun contributeFeedFragment(): FeedFragment

    @ContributesAndroidInjector fun contributeRoomSessionsFragment(): RoomSessionsFragment

    @ContributesAndroidInjector fun contributeScheduleSessionsFragment(): ScheduleSessionsFragment

    @ContributesAndroidInjector fun contributeSearchFragment(): SearchFragment

    @ContributesAndroidInjector fun contributeSearchSessionsFragment(): SearchSessionsFragment

    @ContributesAndroidInjector fun contributeSearchSpeakersFragment(): SearchSpeakersFragment

    @ContributesAndroidInjector fun contributeSearchTopicsFragment(): SearchTopicsFragment

    @ContributesAndroidInjector fun contributeSessionsFragment(): SessionsFragment

    @Binds @IntoMap
    @ViewModelKey(AllSessionsViewModel::class)
    fun bindAllSessionsViewModel(
            allSessionsViewModel: AllSessionsViewModel
    ): ViewModel

    @Binds @IntoMap
    @ViewModelKey(FavoriteSessionsViewModel::class)
    fun bindFavoriteSessionsViewModel(
            favoriteSessionsViewModel: FavoriteSessionsViewModel
    ): ViewModel

    @Binds @IntoMap
    @ViewModelKey(FeedViewModel::class)
    fun bindFeedViewModel(
            feedViewModel: FeedViewModel
    ): ViewModel

    @Binds @IntoMap
    @ViewModelKey(MainViewModel::class)
    fun bindMainViewModel(
            mainViewModel: MainViewModel
    ): ViewModel

    @Binds @IntoMap
    @ViewModelKey(RoomSessionsViewModel::class)
    fun bindRoomSessionsViewModel(
            roomSessionsViewModel: RoomSessionsViewModel
    ): ViewModel

    @Binds @IntoMap
    @ViewModelKey(ScheduleSessionsViewModel::class)
    fun bindScheduleSessionsViewModel(
            scheduleSessionsViewModel: ScheduleSessionsViewModel
    ): ViewModel

    @Binds @IntoMap
    @ViewModelKey(SearchViewModel::class)
    fun bindSearchViewModel(
            searchViewModel: SearchViewModel
    ): ViewModel

    @Binds @IntoMap
    @ViewModelKey(SearchSessionsViewModel::class)
    fun bindSearchSessionViewModel(
            searchSessionsViewModel: SearchSessionsViewModel
    ): ViewModel

    @Binds @IntoMap
    @ViewModelKey(SearchSpeakersViewModel::class)
    fun bindSearchSpeakersViewModel(
            sessionDetailViewModel: SearchSpeakersViewModel
    ): ViewModel

    @Binds @IntoMap
    @ViewModelKey(SearchTopicsViewModel::class)
    fun bindSearchTopicsViewModel(
            sessionDetailViewModel: SearchTopicsViewModel
    ): ViewModel

    @Binds @IntoMap
    @ViewModelKey(SessionsViewModel::class)
    fun bindSessionsViewModel(
            sessionsViewModel: SessionsViewModel
    ): ViewModel
}
