package io.github.droidkaigi.confsched2018.data.repository

import io.github.droidkaigi.confsched2018.model.AboutThisApp
import io.reactivex.Flowable
import io.reactivex.Single


interface AboutThisAppRepository {
    val aboutThisApps: Flowable<List<AboutThisApp>>
}
