package io.github.droidkaigi.confsched2018.util.ext

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import io.github.droidkaigi.confsched2018.util.RxFirestore

fun DocumentReference.observesSnapshot() = RxFirestore.observeDocumentSnapshot(this)
fun DocumentReference.getsSnapshot() = RxFirestore.getDocumentSnapshot(this)
fun DocumentReference.sets(value: Any) = RxFirestore.setDocument(this, value)
fun DocumentReference.deletes() = RxFirestore.deleteDocument(this)

fun CollectionReference.adds(value: Any) = RxFirestore.addDocumentToCollection(this, value)

fun Query.observesSnapshot() = RxFirestore.observeQuerySnapshot(this)
fun Query.getsSnapshot() = RxFirestore.getQuerySnapshot(this)
fun Query.isEmpty() = RxFirestore.isQuerySnapshotEmpty(this)
