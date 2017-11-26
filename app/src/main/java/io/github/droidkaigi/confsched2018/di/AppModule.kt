package io.github.droidkaigi.confsched2018.di


import android.app.Application
import android.arch.persistence.room.Room
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import io.github.droidkaigi.confsched2018.data.api.DroidKaigiApi
import io.github.droidkaigi.confsched2018.data.db.AppDatabase
import io.github.droidkaigi.confsched2018.data.db.SessionDao
import io.github.droidkaigi.confsched2018.data.repository.SessionDataRepository
import io.github.droidkaigi.confsched2018.data.repository.SessionRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module(includes = arrayOf(ViewModelModule::class))
internal class AppModule {
    @Singleton
    @Provides
    fun provideDroidKaigiService(): DroidKaigiApi {
        val httpClient = OkHttpClient
                .Builder()
                .addNetworkInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()
        return Retrofit.Builder()
                .client(httpClient)
                .baseUrl("https://droidkaigi.github.io/2017/")
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setDateFormat("yyyy/MM/dd HH:mm:ss").create()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
                .build()
                .create<DroidKaigiApi>(DroidKaigiApi::class.java)
    }

    @Singleton
    @Provides
    fun provideSessionReposiotry(api: DroidKaigiApi, dbDao: SessionDao): SessionRepository =
            SessionDataRepository(api, dbDao)


    @Singleton
    @Provides
    fun provideDb(app: Application): AppDatabase =
            Room.databaseBuilder(app, AppDatabase::class.java, "droidkaigi.db").build()

    @Singleton
    @Provides
    fun provideSessionsDao(db: AppDatabase): SessionDao = db.sessionDao()


}