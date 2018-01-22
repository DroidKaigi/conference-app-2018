package io.github.droidkaigi.confsched2018.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.github.droidkaigi.confsched2018.presentation.about.AboutThisAppFragment
import io.github.droidkaigi.confsched2018.presentation.contributor.ContributorsFragment
import io.github.droidkaigi.confsched2018.presentation.detail.SessionDetailFragment
import io.github.droidkaigi.confsched2018.presentation.favorite.FavoriteSessionsFragment
import io.github.droidkaigi.confsched2018.presentation.feed.FeedFragment
import io.github.droidkaigi.confsched2018.presentation.map.MapFragment
import io.github.droidkaigi.confsched2018.presentation.search.SearchFragment
import io.github.droidkaigi.confsched2018.presentation.search.SearchSessionsFragment
import io.github.droidkaigi.confsched2018.presentation.search.SearchSpeakersFragment
import io.github.droidkaigi.confsched2018.presentation.search.SearchTopicsFragment
import io.github.droidkaigi.confsched2018.presentation.sessions.AllSessionsFragment
import io.github.droidkaigi.confsched2018.presentation.sessions.RoomSessionsFragment
import io.github.droidkaigi.confsched2018.presentation.sessions.SessionsFragment
import io.github.droidkaigi.confsched2018.presentation.settings.SettingsFragment
import io.github.droidkaigi.confsched2018.presentation.speaker.SpeakerDetailFragment
import io.github.droidkaigi.confsched2018.presentation.sponsors.SponsorsFragment
import io.github.droidkaigi.confsched2018.presentation.staff.StaffFragment
import io.github.droidkaigi.confsched2018.presentation.topic.TopicDetailFragment

@Module interface FragmentBuildersModule {
    @ContributesAndroidInjector fun contributeSessionsFragment(): SessionsFragment

    @ContributesAndroidInjector fun contributeAllSessionsFragment(): AllSessionsFragment

    @ContributesAndroidInjector fun contributeRoomSessionsFragment(): RoomSessionsFragment

    @ContributesAndroidInjector fun contributeSearchFragment(): SearchFragment

    @ContributesAndroidInjector fun contributeSearchSessionsFragment(
    ): SearchSessionsFragment

    @ContributesAndroidInjector fun contributeSearchTopicsFragment(): SearchTopicsFragment

    @ContributesAndroidInjector fun contributeSearchSpeakersFragment(
    ): SearchSpeakersFragment

    @ContributesAndroidInjector fun contributeFavoriteSessionsFragment(
    ): FavoriteSessionsFragment

    @ContributesAndroidInjector fun contributeFeedFragment(): FeedFragment

    @ContributesAndroidInjector fun contributeDetailFragment(): SessionDetailFragment

    @ContributesAndroidInjector fun contributeMapFragment(): MapFragment

    @ContributesAndroidInjector fun contributeAboutThisAppFragment(): AboutThisAppFragment

    @ContributesAndroidInjector fun contributeSponsorsFragment(): SponsorsFragment

    @ContributesAndroidInjector fun contributeSettingsFragment(): SettingsFragment

    @ContributesAndroidInjector fun contributeSpeakerDetailFragment(
    ): SpeakerDetailFragment

    @ContributesAndroidInjector fun contributeTopicDetailFragment(): TopicDetailFragment

    @ContributesAndroidInjector fun contributeContributorFragment(): ContributorsFragment

    @ContributesAndroidInjector fun contributeStaffFragment(): StaffFragment
}
