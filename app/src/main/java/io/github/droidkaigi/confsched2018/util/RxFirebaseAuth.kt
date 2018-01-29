package io.github.droidkaigi.confsched2018.util

import android.support.annotation.CheckResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.github.droidkaigi.confsched2018.util.ext.toSingle
import io.reactivex.Single
import timber.log.Timber

object RxFirebaseAuth {
    @CheckResult fun getCurrentUser(): Single<FirebaseUser> = Single.defer {
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            if (DEBUG) Timber.d("Firestore:Get cached user")
            return@defer Single.just(currentUser)
        }
        auth.signInAnonymously()
                .toSingle()
                .map { it.user }
                .doOnSuccess { if (DEBUG) Timber.d("Firestore:Sign in Anonymously") }
                .doOnError { if (DEBUG) Timber.d("Firestore:Sign in error") }
    }

    private const val DEBUG: Boolean = false
}
