package io.github.droidkaigi.confsched2018.service.push.processor

import com.google.firebase.messaging.RemoteMessage
import javax.inject.Inject

class DefaultProcessor @Inject constructor() : MessageProcessor {
    override fun process(message: RemoteMessage) = Unit
}
