package io.github.droidkaigi.confsched2018.data.repository

import android.support.annotation.CheckResult
import io.github.droidkaigi.confsched2018.model.Contributor
import io.github.droidkaigi.confsched2018.model.Staff
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

interface StaffRepository {
    val staff: Flowable<List<Staff>>
    @CheckResult fun loadStaff(): Completable
}
