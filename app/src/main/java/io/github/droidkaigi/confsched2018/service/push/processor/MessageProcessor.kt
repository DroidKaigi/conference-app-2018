package io.github.droidkaigi.confsched2018.service.push.processor

import com.google.firebase.messaging.RemoteMessage

interface MessageProcessor {
    fun process(message: RemoteMessage)
}
