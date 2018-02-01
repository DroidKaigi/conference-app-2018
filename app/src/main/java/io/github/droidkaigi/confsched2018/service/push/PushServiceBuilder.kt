package io.github.droidkaigi.confsched2018.service.push

import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * @author keishinyokomaku
 */
@Module
interface PushServiceBuilder {
    @ContributesAndroidInjector(modules = [PushServiceModule::class])
    fun contributePushService(): PushService
}
