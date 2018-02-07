package io.github.droidkaigi.confsched2018.di

import android.app.Application
import android.arch.persistence.room.Room
import dagger.Module
import dagger.Provides
import io.github.droidkaigi.confsched2018.data.db.AppDatabase
import io.github.droidkaigi.confsched2018.data.db.ContributorDatabase
import io.github.droidkaigi.confsched2018.data.db.ContributorRoomDatabase
import io.github.droidkaigi.confsched2018.data.db.FavoriteDatabase
import io.github.droidkaigi.confsched2018.data.db.FavoriteFirestoreDatabase
import io.github.droidkaigi.confsched2018.data.db.SessionDatabase
import io.github.droidkaigi.confsched2018.data.db.SessionRoomDatabase
import io.github.droidkaigi.confsched2018.data.db.SponsorDatabase
import io.github.droidkaigi.confsched2018.data.db.SponsorRoomDatabase
import io.github.droidkaigi.confsched2018.data.db.dao.ContributorDao
import io.github.droidkaigi.confsched2018.data.db.dao.SessionDao
import io.github.droidkaigi.confsched2018.data.db.dao.SessionFeedbackDao
import io.github.droidkaigi.confsched2018.data.db.dao.SessionSpeakerJoinDao
import io.github.droidkaigi.confsched2018.data.db.dao.SpeakerDao
import io.github.droidkaigi.confsched2018.data.db.dao.SponsorDao
import javax.inject.Singleton

@Module open class DatabaseModule {

    companion object {
        val instance = DatabaseModule()
    }

    @Singleton @Provides
    fun provideSessionDatabase(
            appDatabase: AppDatabase,
            sessionDbDao: SessionDao,
            speakerDao: SpeakerDao,
            sessionSpeakerJoinDao: SessionSpeakerJoinDao,
            sessionFeedbackDao: SessionFeedbackDao
    ): SessionDatabase =
            SessionRoomDatabase(appDatabase, sessionDbDao, speakerDao, sessionSpeakerJoinDao,
                    sessionFeedbackDao)

    @Singleton @Provides
    fun provideSponsorDatabase(db: AppDatabase, dao: SponsorDao): SponsorDatabase =
            SponsorRoomDatabase(db, dao)

    @Singleton @Provides
    fun provideFavoriteDatabase(): FavoriteDatabase =
            FavoriteFirestoreDatabase()

    @Singleton @Provides
    fun provideContributorsDatabase(db: AppDatabase, dao: ContributorDao): ContributorDatabase =
            ContributorRoomDatabase(db, dao)

    @Singleton @Provides
    open fun provideDb(app: Application): AppDatabase =
            Room.databaseBuilder(app, AppDatabase::class.java, "droidkaigi.db")
                    .fallbackToDestructiveMigration()
                    .build()

    @Singleton @Provides
    fun provideSessionsDao(db: AppDatabase): SessionDao = db.sessionDao()

    @Singleton @Provides
    fun provideSpeakerDao(db: AppDatabase): SpeakerDao = db.speakerDao()

    @Singleton @Provides
    fun provideSessionSpeakerJoinDao(db: AppDatabase): SessionSpeakerJoinDao =
            db.sessionSpeakerDao()

    @Singleton @Provides
    fun provideContributorDao(db: AppDatabase): ContributorDao = db.contributorDao()

    @Singleton @Provides
    fun provideSessionFeedbackDao(db: AppDatabase): SessionFeedbackDao = db.sessionFeedbackDao()

    @Singleton @Provides
    fun provideSponsorDao(db: AppDatabase): SponsorDao = db.sponsorDao()
}
