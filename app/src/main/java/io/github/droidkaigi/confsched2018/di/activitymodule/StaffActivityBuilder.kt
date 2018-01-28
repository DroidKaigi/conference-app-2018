package io.github.droidkaigi.confsched2018.di.activitymodule

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.github.droidkaigi.confsched2018.presentation.staff.StaffActivity

@Module interface StaffActivityBuilder {
    @ContributesAndroidInjector(modules = [StaffActivityModule::class])
    fun contributeStaffActivity(): StaffActivity
}
