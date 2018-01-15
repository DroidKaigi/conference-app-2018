package io.github.droidkaigi.confsched2018.di

import android.app.Application
import android.arch.persistence.room.Room
import dagger.Module
import dagger.Provides
import io.github.droidkaigi.confsched2018.data.db.AppDatabase
import io.github.droidkaigi.confsched2018.data.db.ContributorDatabase
import io.github.droidkaigi.confsched2018.data.db.ContributorRoomDatabase
import io.github.droidkaigi.confsched2018.data.db.FavoriteDatabase
import io.github.droidkaigi.confsched2018.data.db.FavoriteFireStoreDatabase
import io.github.droidkaigi.confsched2018.data.db.SessionDatabase
import io.github.droidkaigi.confsched2018.data.db.SessionRoomDatabase
import io.github.droidkaigi.confsched2018.data.db.dao.ContributorDao
import io.github.droidkaigi.confsched2018.data.db.dao.SessionDao
import io.github.droidkaigi.confsched2018.data.db.dao.SessionSpeakerJoinDao
import io.github.droidkaigi.confsched2018.data.db.dao.SpeakerDao
import javax.inject.Singleton

@Module internal class DatabaseModule {

    @Singleton @Provides
    fun provideSessionDatabase(
            appDatabase: AppDatabase,
            sessionDbDao: SessionDao,
            speakerDao: SpeakerDao,
            sessionSpeakerJoinDao: SessionSpeakerJoinDao
    ): SessionDatabase =
            SessionRoomDatabase(appDatabase, sessionDbDao, speakerDao, sessionSpeakerJoinDao)

    @Singleton @Provides
    fun provideFavoriteDatabase(): FavoriteDatabase =
            FavoriteFireStoreDatabase()

    @Singleton @Provides
    fun provideContributorsDatabase(db: AppDatabase, dao: ContributorDao): ContributorDatabase =
            ContributorRoomDatabase(db, dao)

    @Singleton @Provides
    fun provideDb(app: Application): AppDatabase =
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
}
