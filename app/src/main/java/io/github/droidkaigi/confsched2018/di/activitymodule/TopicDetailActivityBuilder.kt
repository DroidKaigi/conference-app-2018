package io.github.droidkaigi.confsched2018.di.activitymodule

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.github.droidkaigi.confsched2018.presentation.topic.TopicDetailActivity

@Module interface TopicDetailActivityBuilder {
    @ContributesAndroidInjector(modules = [TopicDetailActivityModule::class])
    fun contributeTopicDetailActivity(): TopicDetailActivity
}
