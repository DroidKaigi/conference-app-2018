package io.github.droidkaigi.confsched2018.presentation.search

import android.content.SearchRecentSuggestionsProvider

class SearchSuggestionProvider : SearchRecentSuggestionsProvider() {
    companion object {
        val AUTHORITY: String = "io.github.droidkaigi.confsched2018.presentation.search" +
                ".SearchSuggestionProvider"
        val MODE: Int = DATABASE_MODE_QUERIES
    }

    init {
        this.setupSuggestions(AUTHORITY, MODE)
    }
}
