package io.github.droidkaigi.confsched2018.data.repository

import io.github.droidkaigi.confsched2018.BuildConfig.DEBUG
import io.github.droidkaigi.confsched2018.data.db.fixeddata.AboutThisApps
import io.github.droidkaigi.confsched2018.model.AboutThisApp
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import timber.log.Timber
import javax.inject.Inject

class AboutThisAppDataRepository @Inject constructor() : AboutThisAppRepository {

    override val aboutThisApps: Flowable<List<AboutThisApp>> = Flowable.create({ emitter ->
        if (AboutThisApps.getThisApps().isEmpty()) {
            if (DEBUG) Timber.d("AboutThisApps cannot be empty.")
            emitter.onError(Throwable("AboutThisApps cannot be empty."))
            return@create
        }
        emitter.onNext(AboutThisApps.getThisApps())
    }, BackpressureStrategy.LATEST)
}
