package io.github.droidkaigi.confsched2018.util

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import io.github.droidkaigi.confsched2018.util.ext.toCompletable
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

object RxFirestore {

    fun observeDocumentSnapshot(ref: DocumentReference): Observable<DocumentSnapshot> {
        return Observable.create<DocumentSnapshot> { emitter ->
            val listener = ref.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    emitter.onError(error)
                } else {
                    emitter.onNext(snapshot)
                }
            }
            emitter.setCancellable { listener.remove() }
        }
    }

    fun getDocumentSnapshot(ref: DocumentReference): Single<DocumentSnapshot> {
        return observeDocumentSnapshot(ref).take(1).singleOrError()
    }

    fun setDocument(ref: DocumentReference, value: Any): Completable {
        return Completable.defer { ref.set(value).toCompletable() }
    }

    fun deleteDocument(ref: DocumentReference): Completable {
        return Completable.defer { ref.delete().toCompletable() }
    }

    fun addDocumentToCollection(ref: CollectionReference, value: Any): Completable {
        return Completable.defer { ref.add(value).toCompletable() }
    }

    fun observeQuerySnapshot(ref: Query): Observable<QuerySnapshot> {
        return Observable.create { emitter ->
            val listener = ref.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    emitter.tryOnError(error)
                } else {
                    emitter.onNext(snapshot)
                }
            }
            emitter.setCancellable { listener.remove() }
        }
    }

    fun getQuerySnapshot(ref: Query): Single<QuerySnapshot> {
        return observeQuerySnapshot(ref).take(1).singleOrError()
    }

    fun isQuerySnapshotEmpty(ref: Query): Single<Boolean> {
        return getQuerySnapshot(ref).map { it.isEmpty }
    }
}
