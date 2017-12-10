package io.github.droidkaigi.confsched2018.data.api.response

import com.google.gson.annotations.SerializedName

data class Link(
        @SerializedName("linkType")
        val linkType: String? = null,
        @SerializedName("title")
        val title: String? = null,
        @SerializedName("url")
        val url: String? = null
)
