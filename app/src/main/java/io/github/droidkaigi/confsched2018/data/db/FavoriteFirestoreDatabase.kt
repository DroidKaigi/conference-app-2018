package io.github.droidkaigi.confsched2018.data.db

import android.support.annotation.CheckResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import io.github.droidkaigi.confsched2018.model.Session
import io.github.droidkaigi.confsched2018.util.ext.rx
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import timber.log.Timber

class FavoriteFirestoreDatabase : FavoriteDatabase {

    private var isInitialized = false

    @CheckResult
    private fun getCurrentUser(): Single<FirebaseUser> = Single.create({ e ->
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            if (DEBUG) Timber.d("Firestore:Get cached user")
            e.onSuccess(currentUser)
            return@create
        }
        auth.signInAnonymously()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        if (DEBUG) Timber.d("Firestore:Sign in Anonymously")
                        e.onSuccess(auth.currentUser!!)
                    } else {
                        if (DEBUG) Timber.d("Firestore:Sign in error")
                        e.onError(task.exception!!)
                    }
                }
    })

    override fun favorite(session: Session): Single<Boolean> {
        if (!isInitialized) {
            return Single.error(NotPreparedException())
        }
        return getCurrentUser()
                .flatMap { favoritesRef(it).document(session.id).rx.get() }
                .flatMap { document ->
                    val nowFavorite = document.exists() && (document.data[session.id] == true)
                    val newFavorite = !nowFavorite

                    if (document.exists()) {
                        document.reference.rx
                                .delete()
                                .toSingle { newFavorite }
                    } else {
                        document.reference.rx
                                .set(mapOf("favorite" to newFavorite))
                                .toSingle { newFavorite }
                    }
                }
    }

    @get:CheckResult
    override val favorites: Flowable<List<Int>> = getCurrentUser()
            .flatMap { user: FirebaseUser ->
                return@flatMap setupFavoritesDocument(user)
            }
            .flatMapObservable { user: FirebaseUser ->
                return@flatMapObservable getFavorites(user)
            }
            .doOnNext { isInitialized = true }
            .toFlowable(BackpressureStrategy.DROP)
            .cache()

    @CheckResult
    private fun setupFavoritesDocument(currentUser: FirebaseUser): Single<FirebaseUser> {
        val favorites = favoritesRef(currentUser)
        return favorites.rx.isEmpty().flatMap { isEmpty ->
            if (isEmpty) {
                favorites.rx
                        .add(mapOf("initialized" to true))
                        .onErrorComplete()
                        .toSingle { currentUser }
            } else {
                Single.just(currentUser)
            }
        }
    }

    @CheckResult
    private fun getFavorites(currentUser: FirebaseUser): Observable<List<Int>> {
        return favoritesRef(currentUser)
                .whereEqualTo("favorite", true).rx
                .observe()
                .map { it.documents.mapNotNull { doc -> doc.id.toIntOrNull() } }
    }

    private fun favoritesRef(currentUser: FirebaseUser): CollectionReference {
        val database = FirebaseFirestore.getInstance()
        return database.collection("users/${currentUser.uid}/favorites")
    }

    class NotPreparedException : RuntimeException()

    companion object {
        private const val DEBUG: Boolean = false
    }
}
