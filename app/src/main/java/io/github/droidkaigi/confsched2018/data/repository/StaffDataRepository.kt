package io.github.droidkaigi.confsched2018.data.repository

import android.content.Context
import android.support.annotation.CheckResult
import io.github.droidkaigi.confsched2018.data.local.LocalJsonParser
import io.github.droidkaigi.confsched2018.data.local.StaffJsonMapper
import io.github.droidkaigi.confsched2018.model.Staff
import io.github.droidkaigi.confsched2018.util.rx.SchedulerProvider
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import timber.log.Timber
import javax.inject.Inject

class StaffDataRepository @Inject constructor(
        private val context: Context,
        private val schedulerProvider: SchedulerProvider
) : StaffRepository {
    @CheckResult override fun loadStaff(): Completable = getStaff()
            .subscribeOn(schedulerProvider.io())
            .toCompletable()

    override val staff: Flowable<List<Staff>>
        get() = getStaff().toFlowable().subscribeOn(schedulerProvider.io())

    @CheckResult private fun getStaff(): Single<List<Staff>> {
        return Single.create { emitter ->
            try {
                val asset = LocalJsonParser.loadJsonFromAsset(context, "staff.json")
                emitter.onSuccess(StaffJsonMapper.mapToStaffList(asset))
            } catch (e: Exception) {
                Timber.e(e)
                emitter.onError(e)
            }
        }
    }
}
