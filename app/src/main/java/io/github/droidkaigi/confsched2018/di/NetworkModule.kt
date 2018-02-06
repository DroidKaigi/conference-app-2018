package io.github.droidkaigi.confsched2018.di

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import io.github.droidkaigi.confsched2018.data.api.DroidKaigiApi
import io.github.droidkaigi.confsched2018.data.api.FeedApi
import io.github.droidkaigi.confsched2018.data.api.FeedFirestoreApi
import io.github.droidkaigi.confsched2018.data.api.GithubApi
import io.github.droidkaigi.confsched2018.data.api.SessionFeedbackApi
import io.github.droidkaigi.confsched2018.data.api.response.mapper.ApplicationJsonAdapterFactory
import io.github.droidkaigi.confsched2018.data.api.response.mapper.InstantAdapter
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.threeten.bp.Instant
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module(
        includes = [BuildTypeBasedNetworkModule::class]
)
open class NetworkModule {

    companion object {
        val instance = NetworkModule()
    }

    @Singleton @Provides
    fun provideOkHttpClient(@NetworkLogger loggingInterceptors: Set<@JvmSuppressWildcards
    Interceptor>):
            OkHttpClient =
            OkHttpClient.Builder().apply {
                loggingInterceptors.forEach {
                    addNetworkInterceptor(it)
                }
            }.build()

    @RetrofitDroidKaigi @Singleton @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://droidkaigi.jp/2018/")
                .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder()
                        .add(ApplicationJsonAdapterFactory.INSTANCE)
                        .add(Instant::class.java, InstantAdapter())
                        .build()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
                .build()
    }

    @RetrofitGoogleForm @Singleton @Provides
    fun provideRetrofitForGoogleForm(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://docs.google.com/forms/d/")
                .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder()
                        .add(ApplicationJsonAdapterFactory.INSTANCE)
                        .add(Instant::class.java, InstantAdapter())
                        .build()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
                .build()
    }

    @RetrofitGithub @Singleton @Provides
    fun provideRetrofitForGithub(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .baseUrl("https://api.github.com")
                .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder()
                        .add(ApplicationJsonAdapterFactory.INSTANCE)
                        .add(Instant::class.java, InstantAdapter())
                        .build()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
                .client(okHttpClient)
                .build()
    }

    @Singleton @Provides
    open fun provideDroidKaigiApi(@RetrofitDroidKaigi retrofit: Retrofit): DroidKaigiApi {
        return retrofit.create(DroidKaigiApi::class.java)
    }

    @Singleton @Provides
    fun provideFeedApi(): FeedApi = FeedFirestoreApi()

    @Singleton @Provides
    fun provideSessionFeedbackApi(@RetrofitGoogleForm retrofit: Retrofit): SessionFeedbackApi {
        return retrofit.create(SessionFeedbackApi::class.java)
    }

    @Singleton @Provides
    fun provideGithubApi(@RetrofitGithub retrofit: Retrofit): GithubApi {
        return retrofit.create(GithubApi::class.java)
    }
}
