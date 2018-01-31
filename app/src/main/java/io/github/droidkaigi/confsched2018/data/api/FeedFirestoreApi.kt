package io.github.droidkaigi.confsched2018.data.api

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import io.github.droidkaigi.confsched2018.data.api.response.Post
import io.github.droidkaigi.confsched2018.util.ext.observesSnapshot
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import timber.log.Timber

class FeedFirestoreApi : FeedApi {
    override val feeds: Flowable<List<Post>> = Flowable.defer {
        if (DEBUG) Timber.d("Firestore:getFeeds")

        FirebaseFirestore.getInstance()
                .collection("posts")
                .whereEqualTo("published", true)
                .orderBy("date", Query.Direction.DESCENDING)
                .observesSnapshot()
                .doOnNext { if (DEBUG) Timber.d("Firestore:feed.doOnNext1" + it) }
                .doOnError { if (DEBUG) Timber.d(it, "Firestore:feed.doOnError") }
                .map { it.map { it.toObject(Post::class.java) } }
                .doOnNext { if (DEBUG) Timber.d("Firestore:feed.doOnNext2" + it) }
                .doOnError { if (DEBUG) Timber.d(it, "Firestore:feed.doOnError2") }
                .toFlowable(BackpressureStrategy.DROP)
    }

    companion object {
        private const val DEBUG: Boolean = true
    }
}
