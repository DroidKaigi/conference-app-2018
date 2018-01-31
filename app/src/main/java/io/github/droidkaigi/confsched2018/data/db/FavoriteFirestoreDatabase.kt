package io.github.droidkaigi.confsched2018.data.db

import android.support.annotation.CheckResult
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import io.github.droidkaigi.confsched2018.model.Session
import io.github.droidkaigi.confsched2018.util.RxFirebaseAuth
import io.github.droidkaigi.confsched2018.util.ext.adds
import io.github.droidkaigi.confsched2018.util.ext.deletes
import io.github.droidkaigi.confsched2018.util.ext.getsSnapshot
import io.github.droidkaigi.confsched2018.util.ext.isEmpty
import io.github.droidkaigi.confsched2018.util.ext.observesSnapshot
import io.github.droidkaigi.confsched2018.util.ext.sets
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single

class FavoriteFirestoreDatabase : FavoriteDatabase {

    private var isInitialized = false

    @CheckResult override fun favorite(session: Session): Single<Boolean> {
        if (!isInitialized) {
            return Single.error(NotPreparedException())
        }
        return RxFirebaseAuth.getCurrentUser()
                .flatMap { favoritesRef(it).document(session.id).getsSnapshot() }
                .flatMap { document ->
                    val nowFavorite = document.exists() && (document.data[session.id] == true)
                    val newFavorite = !nowFavorite

                    if (document.exists()) {
                        document.reference
                                .deletes()
                                .toSingle { newFavorite }
                    } else {
                        document.reference
                                .sets(mapOf("favorite" to newFavorite))
                                .toSingle { newFavorite }
                    }
                }
    }

    @get:CheckResult
    override val favorites: Flowable<List<Int>> = RxFirebaseAuth.getCurrentUser()
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
        return favorites.isEmpty().flatMap { isEmpty ->
            if (isEmpty) {
                favorites.adds(mapOf("initialized" to true))
                        .onErrorComplete()
                        .toSingle { currentUser }
            } else {
                Single.just(currentUser)
            }
        }
    }

    @CheckResult private fun getFavorites(currentUser: FirebaseUser): Observable<List<Int>> {
        return favoritesRef(currentUser)
                .whereEqualTo("favorite", true)
                .observesSnapshot()
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
