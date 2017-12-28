package io.github.droidkaigi.confsched2018.presentation

import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.model.Session
import io.github.droidkaigi.confsched2018.presentation.detail.DetailActivity
import io.github.droidkaigi.confsched2018.presentation.detail.DetailFragment
import io.github.droidkaigi.confsched2018.presentation.favorite.FavoriteSessionsFragment
import io.github.droidkaigi.confsched2018.presentation.feed.FeedFragment
import io.github.droidkaigi.confsched2018.presentation.search.SearchFragment
import io.github.droidkaigi.confsched2018.presentation.sessions.SessionsFragment
import javax.inject.Inject

class NavigationController @Inject constructor(private val activity: AppCompatActivity) {
    private val containerId: Int = R.id.content
    private val fragmentManager: FragmentManager = activity.supportFragmentManager

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

    fun navigateToDetail(sessionId: String) {
        fragmentManager
                .beginTransaction()
                .replace(containerId, DetailFragment.newInstance(sessionId))
                .commitAllowingStateLoss()
    }

    fun navigateToDetailActivity(session: Session) {
        DetailActivity.start(activity, session)
    }
}
