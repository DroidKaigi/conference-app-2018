package io.github.droidkaigi.confsched2018.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.github.droidkaigi.confsched2018.presentation.sessions.AllSessionsFragment
import io.github.droidkaigi.confsched2018.presentation.sessions.RoomSessionsFragment
import io.github.droidkaigi.confsched2018.presentation.sessions.SessionsFragment

@Module abstract class FragmentBuildersModule {
    @ContributesAndroidInjector abstract fun contributeSessionsFragment(): SessionsFragment

    @ContributesAndroidInjector abstract fun contributeAllSessionsFragment(): AllSessionsFragment

    @ContributesAndroidInjector abstract fun contributeRoomSessionsFragment(): RoomSessionsFragment
}
