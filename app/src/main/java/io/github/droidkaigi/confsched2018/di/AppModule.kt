package io.github.droidkaigi.confsched2018.di

import android.app.Application
import android.app.NotificationManager
import android.content.Context
import dagger.Module
import dagger.Provides
import io.github.droidkaigi.confsched2018.data.api.DroidKaigiApi
import io.github.droidkaigi.confsched2018.data.db.FavoriteDatabase
import io.github.droidkaigi.confsched2018.data.db.SessionDatabase
import io.github.droidkaigi.confsched2018.data.repository.SessionDataRepository
import io.github.droidkaigi.confsched2018.data.repository.SessionRepository
import io.github.droidkaigi.confsched2018.util.rx.AppSchedulerProvider
import io.github.droidkaigi.confsched2018.util.rx.SchedulerProvider
import javax.inject.Singleton

@Module internal object AppModule {
    @Singleton @Provides @JvmStatic
    fun provideContext(application: Application): Context = application

    @Singleton @Provides @JvmStatic
    fun provideSessionRepository(
            api: DroidKaigiApi,
            sessionDatabase: SessionDatabase,
            favoriteDatabase: FavoriteDatabase,
            schedulerProvider: SchedulerProvider
    ): SessionRepository =
            SessionDataRepository(api, sessionDatabase, favoriteDatabase,
                    schedulerProvider)

    @Singleton @Provides @JvmStatic
    fun provideSchedulerProvider(): SchedulerProvider = AppSchedulerProvider()

    @Singleton
    @Provides
    @JvmStatic
    fun provideNotificationManager(context: Context): NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
}
