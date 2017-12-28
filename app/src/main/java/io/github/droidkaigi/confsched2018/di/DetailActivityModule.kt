package io.github.droidkaigi.confsched2018.di

import android.support.v7.app.AppCompatActivity
import dagger.Binds
import dagger.Module
import io.github.droidkaigi.confsched2018.presentation.detail.DetailActivity

@Module
interface DetailActivityModule {
    @Binds fun providesAppCompatActivity(detailActivity: DetailActivity): AppCompatActivity
}
