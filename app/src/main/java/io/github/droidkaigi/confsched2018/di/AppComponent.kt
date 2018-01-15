package io.github.droidkaigi.confsched2018.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import io.github.droidkaigi.confsched2018.presentation.App
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidInjectionModule::class,
    AppModule::class,
    NetworkModule::class,
    DatabaseModule::class,
    MainActivityBuilder::class,
    MapActivityBuilder::class,
    AboutThisAppActivityBuilder::class,
    ContributorActivityBuilder::class,
    SettingsActivityBuilder::class,
    SponsorsActivityBuilder::class,
    SessionDetailActivityBuilder::class,
    SpeakerDetailActivityBuilder::class,
    TopicDetailActivityBuilder::class
])
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(app: App)
}
