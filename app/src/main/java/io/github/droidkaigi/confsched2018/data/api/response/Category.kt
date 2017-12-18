package io.github.droidkaigi.confsched2018.data.api.response


data class Category(
        val id: Int?,
        val sort: Int?,
        val title: String?,
        val items: List<CategoryItem?>?
)
