package io.github.droidkaigi.confsched2018.test.di

import dagger.Provides
import io.github.droidkaigi.confsched2018.data.api.DroidKaigiApi
import io.github.droidkaigi.confsched2018.di.NetworkModule
import io.github.droidkaigi.confsched2018.test.data.api.StubDroidKaigiApi
import retrofit2.Retrofit

class StubNetworkModule : NetworkModule() {

    @Provides
    override fun provideDroidKaigiApi(retrofit: Retrofit): DroidKaigiApi
        = StubDroidKaigiApi()

}
