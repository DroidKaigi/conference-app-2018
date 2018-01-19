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
import io.github.droidkaigi.confsched2018.data.db.dao.ContributorDao
import io.github.droidkaigi.confsched2018.data.db.dao.SessionDao
import io.github.droidkaigi.confsched2018.data.db.dao.SessionFeedbackDao
import io.github.droidkaigi.confsched2018.data.db.dao.SessionSpeakerJoinDao
import io.github.droidkaigi.confsched2018.data.db.dao.SpeakerDao
import javax.inject.Singleton

@Module internal object DatabaseModule {

    @Singleton @Provides @JvmStatic
    fun provideSessionDatabase(
            appDatabase: AppDatabase,
            sessionDbDao: SessionDao,
            speakerDao: SpeakerDao,
            sessionSpeakerJoinDao: SessionSpeakerJoinDao,
            sessionFeedbackDao: SessionFeedbackDao
    ): SessionDatabase =
            SessionRoomDatabase(appDatabase, sessionDbDao, speakerDao, sessionSpeakerJoinDao,
                    sessionFeedbackDao)

    @Singleton @Provides @JvmStatic
    fun provideFavoriteDatabase(): FavoriteDatabase =
            FavoriteFirestoreDatabase()

    @Singleton @Provides @JvmStatic
    fun provideContributorsDatabase(db: AppDatabase, dao: ContributorDao): ContributorDatabase =
            ContributorRoomDatabase(db, dao)

    @Singleton @Provides @JvmStatic
    fun provideDb(app: Application): AppDatabase =
            Room.databaseBuilder(app, AppDatabase::class.java, "droidkaigi.db")
                    .fallbackToDestructiveMigration()
                    .build()

    @Singleton @Provides @JvmStatic
    fun provideSessionsDao(db: AppDatabase): SessionDao = db.sessionDao()

    @Singleton @Provides @JvmStatic
    fun provideSpeakerDao(db: AppDatabase): SpeakerDao = db.speakerDao()

    @Singleton @Provides @JvmStatic
    fun provideSessionSpeakerJoinDao(db: AppDatabase): SessionSpeakerJoinDao =
            db.sessionSpeakerDao()

    @Singleton @Provides @JvmStatic
    fun provideContributorDao(db: AppDatabase): ContributorDao = db.contributorDao()

    @Singleton @Provides @JvmStatic
    fun provideSessionFeedbackDao(db: AppDatabase): SessionFeedbackDao = db.sessionFeedbackDao()
}
