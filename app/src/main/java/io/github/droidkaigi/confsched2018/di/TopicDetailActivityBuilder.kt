package io.github.droidkaigi.confsched2018.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.github.droidkaigi.confsched2018.presentation.topic.TopicDetailActivity

@Module abstract class TopicDetailActivityBuilder {
    @ContributesAndroidInjector(
            modules = [
                FragmentBuildersModule::class,
                TopicDetailActivityModule::class
            ]
    )
    abstract fun contributeTopicDetailActivity(): TopicDetailActivity
}
