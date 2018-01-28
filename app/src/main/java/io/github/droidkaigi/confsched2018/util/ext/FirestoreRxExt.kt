package io.github.droidkaigi.confsched2018.util.ext

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface RxDocumentReference {
    fun observe(): Observable<DocumentSnapshot>
    fun get(): Single<DocumentSnapshot>
    fun exists(): Single<Boolean>
    fun set(value: Any): Completable
    fun delete(): Completable
}

interface RxCollectionReference {
    fun observe(): Observable<QuerySnapshot>
    fun get(): Single<QuerySnapshot>
    fun isEmpty(): Single<Boolean>
    fun add(value: Any): Completable
}

interface RxQuery {
    fun observe(): Observable<QuerySnapshot>
    fun get(): Single<QuerySnapshot>
    fun isEmpty(): Single<Boolean>
}

val DocumentReference.rx: RxDocumentReference
    get() {
        val ref = this // hack to access outer class
        return object : RxDocumentReference {
            override fun observe(): Observable<DocumentSnapshot> {
                return Observable.create<DocumentSnapshot> { emitter ->
                    val listener = addSnapshotListener { snapshot, error ->
                        if (error != null) {
                            emitter.onError(error)
                        } else {
                            emitter.onNext(snapshot)
                        }
                    }
                    emitter.setCancellable { listener.remove() }
                }
            }

            override fun get(): Single<DocumentSnapshot> {
                return observe().take(1).singleOrError()
            }

            override fun exists(): Single<Boolean> {
                return get().map { it.exists() }
            }

            override fun set(value: Any): Completable {
                return Completable.defer { ref.set(value).toCompletable() }
            }

            override fun delete(): Completable {
                return Completable.defer { ref.delete().toCompletable() }
            }
        }
    }

val CollectionReference.rx: RxCollectionReference
    get() {
        val ref = this // hack to access outer class
        return object : RxCollectionReference {
            override fun observe(): Observable<QuerySnapshot> {
                return (ref as Query).rx.observe()
            }

            override fun get(): Single<QuerySnapshot> {
                return (ref as Query).rx.get()
            }

            override fun isEmpty(): Single<Boolean> {
                return (ref as Query).rx.isEmpty()
            }

            override fun add(value: Any): Completable {
                return Completable.defer { ref.add(value).toCompletable() }
            }
        }
    }

val Query.rx: RxQuery
    get() {
        return object : RxQuery {
            override fun observe(): Observable<QuerySnapshot> {
                return Observable.create { emitter ->
                    val listener = addSnapshotListener { snapshot, error ->
                        if (error != null) {
                            emitter.tryOnError(error)
                        } else {
                            emitter.onNext(snapshot)
                        }
                    }
                    emitter.setCancellable { listener.remove() }
                }
            }

            override fun get(): Single<QuerySnapshot> {
                return observe().take(1).singleOrError()
            }

            override fun isEmpty(): Single<Boolean> {
                return get().map { it.isEmpty }
            }
        }
    }
