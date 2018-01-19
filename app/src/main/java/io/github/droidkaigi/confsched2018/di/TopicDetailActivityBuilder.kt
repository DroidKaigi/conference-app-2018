package io.github.droidkaigi.confsched2018.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.github.droidkaigi.confsched2018.presentation.topic.TopicDetailActivity

@Module interface TopicDetailActivityBuilder {
    @ContributesAndroidInjector(
            modules = [
                FragmentBuildersModule::class,
                TopicDetailActivityModule::class
            ]
    )
    fun contributeTopicDetailActivity(): TopicDetailActivity
}
