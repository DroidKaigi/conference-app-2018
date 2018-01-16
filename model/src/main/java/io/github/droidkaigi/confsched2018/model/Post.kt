package io.github.droidkaigi.confsched2018.model

data class Post(
        val title: String,
        val content: String,
        val date: Date,
        val published: Boolean,
        val type: String
)
