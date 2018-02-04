package io.github.droidkaigi.confsched2018.model

import java.util.Date

data class Post(
        val title: String,
        val content: String,
        val date: Date,
        val published: Boolean,
        val type: Type
) {
    enum class Type {
        TUTORIAL,
        NOTIFICATION,
        ALERT,
        FEEDBACK,
    }
}
