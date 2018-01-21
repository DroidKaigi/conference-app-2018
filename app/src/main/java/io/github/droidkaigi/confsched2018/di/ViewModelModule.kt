package io.github.droidkaigi.confsched2018.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import io.github.droidkaigi.confsched2018.presentation.contributor.ContributorsViewModel
import io.github.droidkaigi.confsched2018.presentation.detail.SessionDetailViewModel
import io.github.droidkaigi.confsched2018.presentation.favorite.FavoriteSessionsViewModel
import io.github.droidkaigi.confsched2018.presentation.feed.FeedViewModel
import io.github.droidkaigi.confsched2018.presentation.search.SearchSessionsViewModel
import io.github.droidkaigi.confsched2018.presentation.search.SearchSpeakersViewModel
import io.github.droidkaigi.confsched2018.presentation.search.SearchTopicsViewModel
import io.github.droidkaigi.confsched2018.presentation.search.SearchViewModel
import io.github.droidkaigi.confsched2018.presentation.sessions.AllSessionsViewModel
import io.github.droidkaigi.confsched2018.presentation.sessions.RoomSessionsViewModel
import io.github.droidkaigi.confsched2018.presentation.sessions.SessionsViewModel
import io.github.droidkaigi.confsched2018.presentation.speaker.SpeakerDetailViewModel
import io.github.droidkaigi.confsched2018.presentation.sponsors.SponsorsViewModel
import io.github.droidkaigi.confsched2018.presentation.topic.TopicDetailViewModel

@Module
interface ViewModelModule {
    @Binds @IntoMap
    @ViewModelKey(AllSessionsViewModel::class)
    fun bindAllSessionsViewModel(allSessionsViewModel: AllSessionsViewModel): ViewModel

    @Binds @IntoMap
    @ViewModelKey(FavoriteSessionsViewModel::class)
    fun bindFavoriteSessionsViewModel(
            favoriteSessionsViewModel: FavoriteSessionsViewModel
    ): ViewModel

    @Binds @IntoMap
    @ViewModelKey(SessionsViewModel::class)
    fun bindSessionsViewModel(sessionsViewModel: SessionsViewModel): ViewModel

    @Binds @IntoMap
    @ViewModelKey(RoomSessionsViewModel::class)
    fun bindRoomSessionsViewModel(roomSessionsViewModel: RoomSessionsViewModel): ViewModel

    @Binds @IntoMap
    @ViewModelKey(SearchSessionsViewModel::class)
    fun bindSearchSessionViewModel(
            searchSessionsViewModel: SearchSessionsViewModel
    ): ViewModel

    @Binds @IntoMap
    @ViewModelKey(SearchViewModel::class)
    fun bindSearchViewModel(searchViewModel: SearchViewModel): ViewModel

    @Binds @IntoMap
    @ViewModelKey(FeedViewModel::class)
    fun bindFeedViewModel(feedViewModel: FeedViewModel): ViewModel

    @Binds @IntoMap
    @ViewModelKey(SessionDetailViewModel::class)
    fun bindSessionDetailViewModel(
            sessionDetailViewModel: SessionDetailViewModel
    ): ViewModel

    @Binds @IntoMap
    @ViewModelKey(SearchTopicsViewModel::class)
    fun bindSearchTopicsViewModel(
            sessionDetailViewModel: SearchTopicsViewModel
    ): ViewModel

    @Binds @IntoMap
    @ViewModelKey(SpeakerDetailViewModel::class)
    fun bindSpeakerDetailViewModel(
            sessionDetailViewModel: SpeakerDetailViewModel
    ): ViewModel

    @Binds @IntoMap
    @ViewModelKey(SearchSpeakersViewModel::class)
    fun bindSearchSpeakersViewModel(
            sessionDetailViewModel: SearchSpeakersViewModel
    ): ViewModel

    @Binds @IntoMap
    @ViewModelKey(ContributorsViewModel::class)
    fun bindContributorsViewModel(
            contributorsViewModel: ContributorsViewModel
    ): ViewModel

    @Binds @IntoMap
    @ViewModelKey(TopicDetailViewModel::class)
    fun bindTopicDetailViewModel(topicDetailViewModel: TopicDetailViewModel): ViewModel

    @Binds @IntoMap
    @ViewModelKey(SponsorsViewModel::class)
    fun bindSponsorsViewModel(sponsorsViewModel: SponsorsViewModel): ViewModel

    @Binds fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}
