package io.github.droidkaigi.confsched2018.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import io.github.droidkaigi.confsched2018.di.activitymodule.AboutThisAppActivityBuilder
import io.github.droidkaigi.confsched2018.di.activitymodule.ContributorsActivityBuilder
import io.github.droidkaigi.confsched2018.di.activitymodule.MainActivityBuilder
import io.github.droidkaigi.confsched2018.di.activitymodule.MapActivityBuilder
import io.github.droidkaigi.confsched2018.di.activitymodule.SessionDetailActivityBuilder
import io.github.droidkaigi.confsched2018.di.activitymodule.SettingsActivityBuilder
import io.github.droidkaigi.confsched2018.di.activitymodule.SpeakerDetailActivityBuilder
import io.github.droidkaigi.confsched2018.di.activitymodule.SponsorsActivityBuilder
import io.github.droidkaigi.confsched2018.di.activitymodule.StaffActivityBuilder
import io.github.droidkaigi.confsched2018.di.activitymodule.TopicDetailActivityBuilder
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
    ContributorsActivityBuilder::class,
    StaffActivityBuilder::class,
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
        fun networkModule(networkModule: NetworkModule): Builder
        fun databaseModule(databaseModule: DatabaseModule): Builder
        fun build(): AppComponent
    }

    fun inject(app: App)
}
