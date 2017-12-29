package io.github.droidkaigi.confsched2018.di

import android.support.v7.app.AppCompatActivity
import dagger.Binds
import dagger.Module
import io.github.droidkaigi.confsched2018.presentation.detail.SessionDetailActivity
import io.github.droidkaigi.confsched2018.presentation.sponsors.SponsorsActivity

@Module interface SponsorsActivityModule {
    @Binds fun providesAppCompatActivity(activity: SponsorsActivity): AppCompatActivity
}
