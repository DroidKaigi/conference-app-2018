package io.github.droidkaigi.confsched2018.test.di

import dagger.Provides
import io.github.droidkaigi.confsched2018.data.api.DroidKaigiApi
import io.github.droidkaigi.confsched2018.di.NetworkModule
import io.github.droidkaigi.confsched2018.test.data.api.MockDroidKaigiApi
import retrofit2.Retrofit

class MockNetworkModule : NetworkModule() {

    @Provides
    override fun provideDroidKaigiApi(retrofit: Retrofit): DroidKaigiApi
        = MockDroidKaigiApi()

}
