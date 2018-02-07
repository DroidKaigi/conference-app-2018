package io.github.droidkaigi.confsched2018.di

import android.app.Application
import android.app.NotificationManager
import android.content.Context
import dagger.Module
import dagger.Provides
import io.github.droidkaigi.confsched2018.data.api.DroidKaigiApi
import io.github.droidkaigi.confsched2018.data.api.FeedApi
import io.github.droidkaigi.confsched2018.data.api.GithubApi
import io.github.droidkaigi.confsched2018.data.api.SessionFeedbackApi
import io.github.droidkaigi.confsched2018.data.db.ContributorDatabase
import io.github.droidkaigi.confsched2018.data.db.FavoriteDatabase
import io.github.droidkaigi.confsched2018.data.db.SessionDatabase
import io.github.droidkaigi.confsched2018.data.db.SponsorDatabase
import io.github.droidkaigi.confsched2018.data.repository.ContributorDataRepository
import io.github.droidkaigi.confsched2018.data.repository.ContributorRepository
import io.github.droidkaigi.confsched2018.data.repository.FeedDataRepository
import io.github.droidkaigi.confsched2018.data.repository.FeedRepository
import io.github.droidkaigi.confsched2018.data.repository.SessionDataRepository
import io.github.droidkaigi.confsched2018.data.repository.SessionRepository
import io.github.droidkaigi.confsched2018.data.repository.SponsorPlanDataRepository
import io.github.droidkaigi.confsched2018.data.repository.SponsorPlanRepository
import io.github.droidkaigi.confsched2018.data.repository.StaffDataRepository
import io.github.droidkaigi.confsched2018.data.repository.StaffRepository
import io.github.droidkaigi.confsched2018.util.rx.AppSchedulerProvider
import io.github.droidkaigi.confsched2018.util.rx.SchedulerProvider
import javax.inject.Singleton

@Module internal object AppModule {
    @Singleton @Provides @JvmStatic
    fun provideContext(application: Application): Context = application

    @Singleton @Provides @JvmStatic
    fun provideSessionRepository(
            api: DroidKaigiApi,
            sessionFeedbackApi: SessionFeedbackApi,
            sessionDatabase: SessionDatabase,
            favoriteDatabase: FavoriteDatabase,
            schedulerProvider: SchedulerProvider
    ): SessionRepository =
            SessionDataRepository(api, sessionFeedbackApi, sessionDatabase, favoriteDatabase,
                    schedulerProvider)

    @Singleton @Provides @JvmStatic
    fun provideFeedRepository(
            feedApi: FeedApi
    ): FeedRepository =
            FeedDataRepository(feedApi)

    @Singleton @Provides @JvmStatic
    fun provideSponsorPlanRepository(
            droidKaigiApi: DroidKaigiApi,
            sponsorDatabase: SponsorDatabase,
            schedulerProvider: SchedulerProvider
    ): SponsorPlanRepository =
            SponsorPlanDataRepository(droidKaigiApi, sponsorDatabase, schedulerProvider)

    @Singleton @Provides @JvmStatic
    fun provideSchedulerProvider(): SchedulerProvider = AppSchedulerProvider()

    @Singleton @Provides @JvmStatic
    fun provideContributorsRepository(
            api: GithubApi,
            contributorDatabase: ContributorDatabase,
            schedulerProvider: SchedulerProvider
    ): ContributorRepository =
            ContributorDataRepository(api, contributorDatabase, schedulerProvider)

    @Singleton @Provides @JvmStatic
    fun provideStaffRepository(
            context: Context,
            schedulerProvider: SchedulerProvider
    ): StaffRepository =
            StaffDataRepository(context, schedulerProvider)

    @Singleton
    @Provides
    @JvmStatic
    fun provideNotificationManager(context: Context): NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
}
