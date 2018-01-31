package io.github.droidkaigi.confsched2018.di.activitymodule

import android.arch.lifecycle.ViewModel
import android.support.v7.app.AppCompatActivity
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import io.github.droidkaigi.confsched2018.di.ViewModelKey
import io.github.droidkaigi.confsched2018.presentation.staff.StaffActivity
import io.github.droidkaigi.confsched2018.presentation.staff.StaffFragment
import io.github.droidkaigi.confsched2018.presentation.staff.StaffViewModel

@Module interface StaffActivityModule {
    @Binds fun providesAppCompatActivity(activity: StaffActivity): AppCompatActivity

    @ContributesAndroidInjector fun contributeStaffFragment(): StaffFragment

    @Binds @IntoMap
    @ViewModelKey(StaffViewModel::class)
    fun bindStaffViewModel(
            staffViewModel: StaffViewModel
    ): ViewModel
}
