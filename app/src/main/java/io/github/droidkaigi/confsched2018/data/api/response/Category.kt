package io.github.droidkaigi.confsched2018.data.api.response

import com.google.gson.annotations.SerializedName

data class Category(
        @SerializedName("id") val id: Int? = null,
        @SerializedName("sort") val sort: Int? = null,
        @SerializedName("title") val title: String? = null,
        @SerializedName("items") val items: List<CategoryItem?>? = null
)
