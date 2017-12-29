package io.github.droidkaigi.confsched2018.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.github.droidkaigi.confsched2018.presentation.about.AboutThisAppFragment
import io.github.droidkaigi.confsched2018.presentation.detail.SessionDetailFragment
import io.github.droidkaigi.confsched2018.presentation.favorite.FavoriteSessionsFragment
import io.github.droidkaigi.confsched2018.presentation.feed.FeedFragment
import io.github.droidkaigi.confsched2018.presentation.map.MapFragment
import io.github.droidkaigi.confsched2018.presentation.search.SearchFragment
import io.github.droidkaigi.confsched2018.presentation.search.SearchSessionFragment
import io.github.droidkaigi.confsched2018.presentation.sessions.AllSessionsFragment
import io.github.droidkaigi.confsched2018.presentation.sessions.RoomSessionsFragment
import io.github.droidkaigi.confsched2018.presentation.sessions.SessionsFragment
import io.github.droidkaigi.confsched2018.presentation.settings.SettingsFragment
import io.github.droidkaigi.confsched2018.presentation.sponsors.SponsorsFragment

@Module abstract class FragmentBuildersModule {
    @ContributesAndroidInjector abstract fun contributeSessionsFragment(): SessionsFragment

    @ContributesAndroidInjector abstract fun contributeAllSessionsFragment(): AllSessionsFragment

    @ContributesAndroidInjector abstract fun contributeRoomSessionsFragment(): RoomSessionsFragment

    @ContributesAndroidInjector abstract fun contributeSearchFragment(): SearchFragment

    @ContributesAndroidInjector abstract fun contributeSearchSessionFragment(): SearchSessionFragment

    @ContributesAndroidInjector abstract fun contributeFavoriteSessionsFragment(): FavoriteSessionsFragment

    @ContributesAndroidInjector abstract fun contributeFeedFragment(): FeedFragment

    @ContributesAndroidInjector abstract fun contributeDetailFragment(): SessionDetailFragment

    @ContributesAndroidInjector abstract fun contributeMapFragment(): MapFragment

    @ContributesAndroidInjector abstract fun contributeAboutThisAppFragment(): AboutThisAppFragment

    @ContributesAndroidInjector abstract fun contributeSponsorsFragment(): SponsorsFragment

    @ContributesAndroidInjector abstract fun contributeSettingsFragment(): SettingsFragment
}
