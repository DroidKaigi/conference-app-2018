package io.github.droidkaigi.confsched2018.presentation.search

import android.content.SearchRecentSuggestionsProvider
import io.github.droidkaigi.confsched2018.BuildConfig

class SearchSuggestionProvider : SearchRecentSuggestionsProvider() {
    companion object {
        val AUTHORITY: String =
                "${BuildConfig.APPLICATION_ID}.presentation.search.SearchSuggestionProvider"
        val MODE: Int = DATABASE_MODE_QUERIES
    }

    init {
        this.setupSuggestions(AUTHORITY, MODE)
    }
}
