package io.github.droidkaigi.confsched2018.presentation

import android.support.v4.app.FragmentManager
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.presentation.favorite.FavoriteSessionsFragment
import io.github.droidkaigi.confsched2018.presentation.feed.FeedFragment
import io.github.droidkaigi.confsched2018.presentation.search.SearchFragment
import io.github.droidkaigi.confsched2018.presentation.sessions.SessionsFragment
import javax.inject.Inject

class NavigationController @Inject constructor(mainActivity: MainActivity) {
    private val containerId: Int
    private val fragmentManager: FragmentManager

    init {
        this.containerId = R.id.content
        this.fragmentManager = mainActivity.supportFragmentManager
    }

    fun navigateToSessions() {
        fragmentManager
                .beginTransaction()
                .replace(containerId, SessionsFragment.newInstance())
                .commitAllowingStateLoss()
    }

    fun navigateToSearch() {
        fragmentManager
                .beginTransaction()
                .replace(containerId, SearchFragment.newInstance())
                .commitAllowingStateLoss()
    }

    fun navigateToFavoriteSessions() {
        fragmentManager
                .beginTransaction()
                .replace(containerId, FavoriteSessionsFragment.newInstance())
                .commitAllowingStateLoss()
    }

    fun navigateToFeed() {
        fragmentManager
                .beginTransaction()
                .replace(containerId, FeedFragment.newInstance())
                .commitAllowingStateLoss()
    }
}
