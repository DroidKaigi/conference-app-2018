package io.github.droidkaigi.confsched2018.di


import android.app.Application
import android.arch.persistence.room.Room
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import io.github.droidkaigi.confsched2018.data.api.DroidKaigiApi
import io.github.droidkaigi.confsched2018.data.db.AppDatabase
import io.github.droidkaigi.confsched2018.data.db.dao.SessionDao
import io.github.droidkaigi.confsched2018.data.db.dao.SessionSpeakerJoinDao
import io.github.droidkaigi.confsched2018.data.db.dao.SpeakerDao
import io.github.droidkaigi.confsched2018.data.repository.SessionDataRepository
import io.github.droidkaigi.confsched2018.data.repository.SessionRepository
import io.github.droidkaigi.confsched2018.util.rx.AppSchedulerProvider
import io.github.droidkaigi.confsched2018.util.rx.SchedulerProvider
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module(includes = arrayOf(ViewModelModule::class))
internal class AppModule {
    @Singleton @Provides
    fun provideDroidKaigiService(): DroidKaigiApi {
        val httpClient = OkHttpClient
                .Builder()
                .addNetworkInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()
        return Retrofit.Builder()
                .client(httpClient)
                .baseUrl("https://sessionize.com/api/v2/rxafxyj8/view/")
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setDateFormat("yyyy/MM/dd HH:mm:ss").create()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
                .build()
                .create<DroidKaigiApi>(DroidKaigiApi::class.java)
    }

    @Singleton @Provides
    fun provideSessionReposiotry(
            api: DroidKaigiApi,
            sessionDbDao: SessionDao,
            speakerDao: SpeakerDao,
            sessionSpeakerJoinDao: SessionSpeakerJoinDao,
            schedulerProvider: SchedulerProvider
    ): SessionRepository =
            SessionDataRepository(api, sessionDbDao, speakerDao, sessionSpeakerJoinDao, schedulerProvider)


    @Singleton @Provides
    fun provideDb(app: Application): AppDatabase =
            Room.databaseBuilder(app, AppDatabase::class.java, "droidkaigi.db").build()

    @Singleton @Provides
    fun provideSessionsDao(db: AppDatabase): SessionDao = db.sessionDao()

    @Singleton @Provides
    fun provideSpeakerDao(db: AppDatabase): SpeakerDao = db.speakerDao()

    @Singleton @Provides
    fun provideSessionSpeakerJoinDao(db: AppDatabase): SessionSpeakerJoinDao = db.sessionSpeakerDao()

    @Singleton @Provides
    fun provideSchedulerProvider(): SchedulerProvider = AppSchedulerProvider()

}