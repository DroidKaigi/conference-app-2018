package io.github.droidkaigi.confsched2018.data.db

import android.support.annotation.CheckResult
import io.github.droidkaigi.confsched2018.model.Session
import io.reactivex.Flowable
import io.reactivex.Single

interface FavoriteDatabase {

    @get:CheckResult val favorites: Flowable<List<Int>>

    @CheckResult fun favorite(session: Session): Single<Boolean>
}
