package io.github.droidkaigi.confsched2018.service.push

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.android.AndroidInjection
import timber.log.Timber
import javax.inject.Inject

class PushService : FirebaseMessagingService() {
    @Inject
    lateinit var classifier: MessageClassifier

    override fun onCreate() {
        super.onCreate()
        AndroidInjection.inject(this)
    }

    override fun onMessageReceived(message: RemoteMessage?) {
        message?.let {
            Timber.d("from: %s", message.from)
            classifier.classify(message).process(message)
        }
    }
}
