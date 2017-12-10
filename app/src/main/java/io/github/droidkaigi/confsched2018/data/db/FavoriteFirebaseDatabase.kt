package io.github.droidkaigi.confsched2018.data.db

import android.support.annotation.CheckResult
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import io.github.droidkaigi.confsched2018.model.Session
import io.reactivex.*
import io.reactivex.android.MainThreadDisposable

class FavoriteFirebaseDatabase : FavoriteDatabase {


    private var isInitialized = false

    @CheckResult
    private fun getCurrentUser(): Single<FirebaseUser> = Single.create({ e ->
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            e.onSuccess(currentUser)
            return@create
        }
        auth.signInAnonymously()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        e.onSuccess(auth.currentUser!!)
                    } else {
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
                val favorites = database.document("users/" + currentUser.uid + "/sessions/favorite")
                favorites.get().addOnCompleteListener { task: Task<DocumentSnapshot> ->
                    if (task.isSuccessful) {
                        val result = task.result
                        val nowFavorite = result.exists() && (result.data[session.id] as? Boolean ?: false)
                        val newFavorite = !nowFavorite

                        val completeListener: (Task<Void>) -> Unit = {
                            val exception = it.exception
                            if (exception != null) {
                                e.onError(exception)
                            } else {
                                e.onSuccess(newFavorite)
                            }
                        }

                        val sessionToFavoriteMap = mapOf(session.id to newFavorite)
                        if (result.exists()) {
                            favorites.update(sessionToFavoriteMap).addOnCompleteListener(completeListener)
                        } else {
                            favorites.set(sessionToFavoriteMap).addOnCompleteListener(completeListener)
                        }
                    } else {
                        e.onError(task.exception!!)
                    }
                }
            })
        }
    }


    @get:CheckResult
    override val favorites: Flowable<List<Int>> = getCurrentUser()
            .flatMap { return@flatMap setupFavoritesDocument(it) }
            .flatMapObservable { return@flatMapObservable getFavorites(it) }
            .doOnNext { isInitialized = true }
            .retry(2)
            .toFlowable(BackpressureStrategy.DROP)

    @CheckResult
    private fun setupFavoritesDocument(currentUser: FirebaseUser): Single<FirebaseUser> {
        return Single.create({ e: SingleEmitter<FirebaseUser> ->
            val database = FirebaseFirestore.getInstance()
            val favorites = database.document("users/" + currentUser.uid + "/sessions/favorite")
            favorites.get().addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    e.onError(task.exception!!)
                    return@addOnCompleteListener
                }
                if (!task.result.exists()) {
                    // FIXME: I want to create document without setting value
                    favorites.set(mapOf("-1" to false)).addOnCompleteListener {
                        e.onSuccess(currentUser)
                    }
                } else {
                    e.onSuccess(currentUser)
                }

            }
            return@create
        })
    }

    @CheckResult
    private fun getFavorites(currentUser: FirebaseUser): Observable<List<Int>> {
        return Observable.create<List<Int>>({ e: ObservableEmitter<List<Int>> ->
            val database = FirebaseFirestore.getInstance()
            val favorites = database.document("users/" + currentUser.uid + "/sessions/favorite")
            val removable = favorites.addSnapshotListener(EventListener { documentSnapshot, exception ->
                if (exception != null) {
                    exception.printStackTrace()
                    return@EventListener
                }
                if (documentSnapshot.exists()) {
                    @Suppress("UNCHECKED_CAST")
                    val map = documentSnapshot.data as Map<String, Boolean>
                    e.onNext(map.filter { entry -> entry.value }.map { it.key.toInt() }.toList())
                } else {
                    e.onNext(listOf())
                }


            })

            e.setDisposable(object : MainThreadDisposable() {
                override fun onDispose() {
                    removable.remove()
                }
            })
        })
    }

    class NotPrepairedException : RuntimeException()
}
