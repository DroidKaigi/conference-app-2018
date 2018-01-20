package io.github.droidkaigi.confsched2018.di.activitymodule

import android.support.v7.app.AppCompatActivity
import dagger.Binds
import dagger.Module
import io.github.droidkaigi.confsched2018.presentation.detail.SessionDetailActivity

@Module interface SessionDetailActivityModule {
    @Binds fun providesAppCompatActivity(activity: SessionDetailActivity): AppCompatActivity
}
