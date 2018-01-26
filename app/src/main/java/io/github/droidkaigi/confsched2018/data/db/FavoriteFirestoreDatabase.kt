package io.github.droidkaigi.confsched2018.data.db

import android.support.annotation.CheckResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import io.github.droidkaigi.confsched2018.model.Session
import io.github.droidkaigi.confsched2018.util.ext.toSingle
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.android.MainThreadDisposable
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

    override fun favorite(session: Session): Single<Boolean> = if (!isInitialized) {
        Single.error(NotPreparedException())
    } else {
        getCurrentUser().flatMap { currentUser ->
            return@flatMap Single.create<DocumentSnapshot>({ e ->
                val database = FirebaseFirestore.getInstance()
                val favorites = database.collection("users/${currentUser.uid}/favorites")
                        .document(session.id)
                val listener = favorites.addSnapshotListener { documentSnapshot, exception ->
                    if (DEBUG) Timber.d("Firestore:favorite get")
                    if (exception != null) {
                        e.onError(exception)
                    } else {
                        e.onSuccess(documentSnapshot)
                    }
                }
                e.setCancellable {
                    if (DEBUG) Timber.d("Firestore:favorite dispose")
                    listener.remove()
                }

            // To avoid get -> write -> get -> ... loop, split task.
            }).flatMap<Boolean> { documentSnapshot ->
                val nowFavorite = documentSnapshot.exists() && (documentSnapshot.data[session.id] == true)
                val newFavorite = !nowFavorite

                val sessionToFavoriteMap = mapOf("favorite" to newFavorite)
                if (documentSnapshot.exists()) {
                    documentSnapshot.reference.delete()
                            .toSingle()
                            .map { newFavorite }
                } else {
                    documentSnapshot.reference.set(sessionToFavoriteMap)
                            .toSingle()
                            .map { newFavorite }
                }
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
        val database = FirebaseFirestore.getInstance()
        val favorites = database.collection("users/" + currentUser.uid + "/favorites")

        return Single.create({ e: SingleEmitter<QuerySnapshot> ->
            if (DEBUG) Timber.d("Firestore:setupFavoritesDocument")
            val listener = favorites.addSnapshotListener { querySnapshot, exception ->
                if (DEBUG) Timber.d("Firestore:setupFavoritesDocument onComplete")
                if (exception != null) {
                    Timber.e(exception, "Firestore:setupFavoritesDocument onComplete fail ")
                    e.onError(exception)
                } else {
                    e.onSuccess(querySnapshot)
                }
            }
            e.setCancellable {
                if (DEBUG) Timber.d("Firestore:setupFavoritesDocument dispose")
                listener.remove()
            }

        // To avoid get -> write -> get -> ... loop, split task.
        }).flatMap { querySnapshot ->
            Single.create<FirebaseUser> { e ->
                if (querySnapshot.isEmpty) {
                    favorites.add(mapOf("initialized" to true)).addOnCompleteListener {
                        if (DEBUG) Timber.d("Firestore:create document for listing")
                        e.onSuccess(currentUser)
                    }
                } else {
                    if (DEBUG) Timber.d("Firestore:document already exists")
                    e.onSuccess(currentUser)
                }
            }
        }
    }

    @CheckResult
    private fun getFavorites(currentUser: FirebaseUser): Observable<List<Int>> {
        return Observable.create<List<Int>>({ e: ObservableEmitter<List<Int>> ->
            if (e.isDisposed) {
                e.onComplete()
            }
            if (DEBUG) Timber.d("Firestore:getFavorites")
            val database = FirebaseFirestore.getInstance()
            val favorites = database.collection("users/" + currentUser.uid + "/favorites")
                    .whereEqualTo("favorite", true)
            val eventListener = EventListener<QuerySnapshot> { documentSnapshot, exception ->
                if (exception != null) {
                    if (DEBUG) Timber.d("Firestore:getFavorites onChange exception")
                    e.onError(exception)
                    return@EventListener
                }
                if (!documentSnapshot.isEmpty) {
                    if (DEBUG) Timber.d("Firestore:getFavorites onChange")
                    val documents = documentSnapshot.documents
                    val favoriteSessionIds = documents
                            .mapNotNull { document -> document.id.toIntOrNull() }
                            .toList()
                    if (DEBUG) Timber.d("Firestore:getFavorites onChange %s", favoriteSessionIds)
                    e.onNext(favoriteSessionIds)
                } else {
                    if (DEBUG) Timber.d("Firestore:getFavorites return empty")
                    e.onNext(listOf())
                }
            }
            val removable = favorites.addSnapshotListener(eventListener)

            e.setDisposable(object : MainThreadDisposable() {
                override fun onDispose() {
                    if (DEBUG) Timber.d("Firestore:getFavorites dispose")
                    removable.remove()
                }
            })
        })
    }

    class NotPreparedException : RuntimeException()

    companion object {
        private const val DEBUG: Boolean = false
    }
}
