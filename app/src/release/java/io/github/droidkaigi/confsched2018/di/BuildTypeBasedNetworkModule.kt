package io.github.droidkaigi.confsched2018.di

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
internal class BuildTypeBasedNetworkModule {

    @NetworkLogger @Singleton @Provides @IntoSet
    fun provideNetworkLogger(): Interceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BASIC
    }
}
