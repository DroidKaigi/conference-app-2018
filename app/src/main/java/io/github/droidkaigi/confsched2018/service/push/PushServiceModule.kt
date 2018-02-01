package io.github.droidkaigi.confsched2018.service.push

import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector

@Module
interface PushServiceModule : AndroidInjector<PushService> {
    @Binds fun providesService(service: PushService): PushService
}
