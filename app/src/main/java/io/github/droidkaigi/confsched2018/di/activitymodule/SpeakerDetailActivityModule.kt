package io.github.droidkaigi.confsched2018.di.activitymodule

import android.arch.lifecycle.ViewModel
import android.support.v7.app.AppCompatActivity
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import io.github.droidkaigi.confsched2018.di.ViewModelKey
import io.github.droidkaigi.confsched2018.presentation.speaker.SpeakerDetailActivity
import io.github.droidkaigi.confsched2018.presentation.speaker.SpeakerDetailFragment
import io.github.droidkaigi.confsched2018.presentation.speaker.SpeakerDetailViewModel

@Module interface SpeakerDetailActivityModule {
    @Binds fun providesAppCompatActivity(activity: SpeakerDetailActivity): AppCompatActivity

    @ContributesAndroidInjector fun contributeSpeakerDetailFragment(): SpeakerDetailFragment

    @Binds @IntoMap
    @ViewModelKey(SpeakerDetailViewModel::class)
    fun bindSpeakerDetailViewModel(
            sessionDetailViewModel: SpeakerDetailViewModel
    ): ViewModel
}
