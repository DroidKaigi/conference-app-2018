package io.github.droidkaigi.confsched2018.service.push

import com.google.firebase.messaging.RemoteMessage
import dagger.Lazy
import io.github.droidkaigi.confsched2018.service.push.processor.DefaultProcessor
import io.github.droidkaigi.confsched2018.service.push.processor.MessageProcessor
import io.github.droidkaigi.confsched2018.service.push.processor.NewPostProcessor
import javax.inject.Inject

class MessageClassifier @Inject constructor(
        private val postProcessor: Lazy<NewPostProcessor>,
        private val defaultProcessor: Lazy<DefaultProcessor>) {
    fun classify(remoteMessage: RemoteMessage): MessageProcessor {
        return when (remoteMessage.from) {
            NewPostProcessor.FCM_FROM -> {
                postProcessor.get()
            }
            else -> {
                defaultProcessor.get()
            }
        }
    }
}
