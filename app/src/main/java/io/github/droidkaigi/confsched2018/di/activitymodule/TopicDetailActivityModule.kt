package io.github.droidkaigi.confsched2018.di.activitymodule

import android.arch.lifecycle.ViewModel
import android.support.v7.app.AppCompatActivity
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import io.github.droidkaigi.confsched2018.di.ViewModelKey
import io.github.droidkaigi.confsched2018.presentation.topic.TopicDetailActivity
import io.github.droidkaigi.confsched2018.presentation.topic.TopicDetailFragment
import io.github.droidkaigi.confsched2018.presentation.topic.TopicDetailViewModel

@Module interface TopicDetailActivityModule {
    @Binds fun providesAppCompatActivity(activity: TopicDetailActivity): AppCompatActivity

    @ContributesAndroidInjector fun contributeTopicDetailFragment(): TopicDetailFragment

    @Binds @IntoMap
    @ViewModelKey(TopicDetailViewModel::class)
    fun bindTopicDetailViewModel(topicDetailViewModel: TopicDetailViewModel): ViewModel
}
