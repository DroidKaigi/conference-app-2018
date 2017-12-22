package io.github.droidkaigi.confsched2018.data.api

import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import io.github.droidkaigi.confsched2018.data.api.response.Post
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableEmitter
import io.reactivex.android.MainThreadDisposable
import timber.log.Timber

class FeedFireStoreApi : FeedApi {
    override val feeds: Flowable<List<Post>> =
            Flowable.create<List<Post>>({ e: FlowableEmitter<List<Post>> ->
                if (e.isCancelled) {
                    e.onComplete()
                }
                if (DEBUG) Timber.d("FireStore:getFeeds")
                val database = FirebaseFirestore.getInstance()
                val postCollection = database.collection("posts")
                        .whereEqualTo("published", true)
                        .orderBy("date", Query.Direction.DESCENDING)
                val removable = postCollection.addSnapshotListener(EventListener { documentSnapshot, exception ->
                    if (exception != null) {
                        if (DEBUG) Timber.d("FireStore:getFeeds onChange exception")
                        e.onError(exception)
                        return@EventListener
                    }
                    if (!documentSnapshot.isEmpty) {
                        if (DEBUG) Timber.d("FireStore:getFeeds onChange")
                        val documents = documentSnapshot.documents
                        val posts = documents.map { it.toObject(Post::class.java) }
                        if (DEBUG) Timber.d("FireStore:getFeeds onChange %s", posts.toString())
                        e.onNext(posts)
                    } else {
                        if (DEBUG) Timber.d("FireStore:getFeeds return empty")
                        e.onNext(listOf())
                    }

                })

                e.setDisposable(object : MainThreadDisposable() {
                    override fun onDispose() {
                        if (DEBUG) Timber.d("FireStore:getFeeds dispose")
                        removable.remove()
                    }
                })
            }, BackpressureStrategy.DROP)

    companion object {
        private const val DEBUG: Boolean = true
    }
}
