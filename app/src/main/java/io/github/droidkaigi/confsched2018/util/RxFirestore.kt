package io.github.droidkaigi.confsched2018.util

import android.support.annotation.CheckResult
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

    @CheckResult fun observeDocumentSnapshot(ref: DocumentReference): Observable<DocumentSnapshot> {
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

    @CheckResult fun getDocumentSnapshot(ref: DocumentReference): Single<DocumentSnapshot> {
        return observeDocumentSnapshot(ref).take(1).singleOrError()
    }

    @CheckResult fun setDocument(ref: DocumentReference, value: Any): Completable {
        return Completable.defer { ref.set(value).toCompletable() }
    }

    @CheckResult fun deleteDocument(ref: DocumentReference): Completable {
        return Completable.defer { ref.delete().toCompletable() }
    }

    @CheckResult fun addDocumentToCollection(ref: CollectionReference, value: Any): Completable {
        return Completable.defer { ref.add(value).toCompletable() }
    }

    @CheckResult fun observeQuerySnapshot(ref: Query): Observable<QuerySnapshot> {
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

    @CheckResult fun getQuerySnapshot(ref: Query): Single<QuerySnapshot> {
        return observeQuerySnapshot(ref).take(1).singleOrError()
    }

    @CheckResult fun isQuerySnapshotEmpty(ref: Query): Single<Boolean> {
        return getQuerySnapshot(ref).map { it.isEmpty }
    }
}
