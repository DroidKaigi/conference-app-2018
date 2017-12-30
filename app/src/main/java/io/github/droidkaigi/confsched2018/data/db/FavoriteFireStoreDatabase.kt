package io.github.droidkaigi.confsched2018.data.db

import android.support.annotation.CheckResult
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import io.github.droidkaigi.confsched2018.model.Session
import io.reactivex.*
import io.reactivex.android.MainThreadDisposable
import timber.log.Timber

class FavoriteFireStoreDatabase : FavoriteDatabase {

    private var isInitialized = false

    @CheckResult
    private fun getCurrentUser(): Single<FirebaseUser> = Single.create({ e ->
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            if (DEBUG) Timber.d("FireStore:Get chached user")
            e.onSuccess(currentUser)
            return@create
        }
        auth.signInAnonymously()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        if (DEBUG) Timber.d("FireStore:Sign in Anonymously")
                        e.onSuccess(auth.currentUser!!)
                    } else {
                        if (DEBUG) Timber.d("FireStore:Sign in error")
                        e.onError(task.exception!!)
                    }
                }
    })

    override fun favorite(session: Session): Single<Boolean> = if (!isInitialized) {
        Single.error(NotPrepairedException())
    } else {
        getCurrentUser().flatMap { currentUser ->
            return@flatMap Single.create<Boolean>({ e ->
                val database = FirebaseFirestore.getInstance()
                val favorites = database.collection("users/${currentUser.uid}/favorites")
                        .document(session.id)
                favorites.get().addOnCompleteListener(OnCompleteListener { task: Task<DocumentSnapshot> ->
                    if (DEBUG) Timber.d("FireStore:favorite get")
                    if (task.isSuccessful) {
                        val result = task.result
                        val nowFavorite = result.exists() && (result.data[session.id] as? Boolean ?: false)
                        val newFavorite = !nowFavorite

                        val completeListener: (Task<Void>) -> Unit = {
                            val exception = it.exception
                            if (exception != null) {
                                if (DEBUG) Timber.d(exception, "FireStore:favorite wirte fail")
                                e.onError(exception)
                            } else {
                                if (DEBUG) Timber.d("FireStore:favorite wirte success")
                                e.onSuccess(newFavorite)
                            }
                        }

                        val sessionToFavoriteMap = mapOf("favorite" to newFavorite)
                        if (result.exists()) {
                            favorites.delete().addOnCompleteListener(completeListener)
                        } else {
                            favorites.set(sessionToFavoriteMap).addOnCompleteListener(completeListener)
                        }
                    } else {
                        e.onError(task.exception!!)
                    }
                })
            })
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
        return Single.create({ e: SingleEmitter<FirebaseUser> ->
            if (DEBUG) Timber.d("FireStore:setupFavoritesDocument")
            val database = FirebaseFirestore.getInstance()
            val favorites = database.collection("users/" + currentUser.uid + "/favorites")
            favorites.get().addOnCompleteListener { task ->
                if (DEBUG) Timber.d("FireStore:setupFavoritesDocument onComplete")
                if (!task.isSuccessful) {
                    Timber.e(task.exception!!, "FireStore:setupFavoritesDocument onComplete fail ")
                    e.onError(task.exception!!)
                    return@addOnCompleteListener
                }
                if (!task.result.isEmpty) {
                    // FIXME: I want to create document without setting value
                    favorites.add(mapOf("initialized" to true)).addOnCompleteListener {
                        if (DEBUG) Timber.d("FireStore:create document for listeing")
                        e.onSuccess(currentUser)
                    }
                } else {
                    if (DEBUG) Timber.d("FireStore:document already exists")
                    e.onSuccess(currentUser)
                }
            }
            return@create
        })
    }

    @CheckResult
    private fun getFavorites(currentUser: FirebaseUser): Observable<List<Int>> {
        return Observable.create<List<Int>>({ e: ObservableEmitter<List<Int>> ->
            if (e.isDisposed) {
                e.onComplete()
            }
            if (DEBUG) Timber.d("FireStore:getFavorites")
            val database = FirebaseFirestore.getInstance()
            val favorites = database.collection("users/" + currentUser.uid + "/favorites")
                    .whereEqualTo("favorite", true)
            val removable = favorites.addSnapshotListener(EventListener { documentSnapshot, exception ->
                if (exception != null) {
                    if (DEBUG) Timber.d("FireStore:getFavorites onChange exception")
                    e.onError(exception)
                    return@EventListener
                }
                if (!documentSnapshot.isEmpty) {
                    if (DEBUG) Timber.d("FireStore:getFavorites onChange")
                    val documents = documentSnapshot.documents
                    val favoriteSessionIds = documents
                            .mapNotNull { document -> document.id.toIntOrNull() }
                            .toList()
                    if (DEBUG) Timber.d("FireStore:getFavorites onChange %s", favoriteSessionIds.toString())
                    e.onNext(favoriteSessionIds)
                } else {
                    if (DEBUG) Timber.d("FireStore:getFavorites return empty")
                    e.onNext(listOf())
                }
            })

            e.setDisposable(object : MainThreadDisposable() {
                override fun onDispose() {
                    if (DEBUG) Timber.d("FireStore:getFavorites dispose")
                    removable.remove()
                }
            })
        })
    }

    class NotPrepairedException : RuntimeException()

    companion object {
        private const val DEBUG: Boolean = false
    }
}
