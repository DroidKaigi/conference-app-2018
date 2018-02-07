package io.github.droidkaigi.confsched2018.presentation.search

import android.app.Activity
import android.app.SearchManager
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.database.CrossProcessCursor
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.support.v7.widget.SimpleItemAnimator
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.view.forEach
import com.google.firebase.analytics.FirebaseAnalytics
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import dagger.android.support.DaggerFragment
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.databinding.FragmentSearchBinding
import io.github.droidkaigi.confsched2018.model.Session
import io.github.droidkaigi.confsched2018.presentation.FragmentStateNullablePagerAdapter
import io.github.droidkaigi.confsched2018.presentation.NavigationController
import io.github.droidkaigi.confsched2018.presentation.Result
import io.github.droidkaigi.confsched2018.presentation.common.view.OnTabReselectedDispatcher
import io.github.droidkaigi.confsched2018.presentation.search.item.SearchResultSpeakerItem
import io.github.droidkaigi.confsched2018.presentation.search.item.SearchSpeakersSection
import io.github.droidkaigi.confsched2018.presentation.sessions.item.SimpleSessionsSection
import io.github.droidkaigi.confsched2018.presentation.sessions.item.SpeechSessionItem
import io.github.droidkaigi.confsched2018.util.SessionAlarm
import io.github.droidkaigi.confsched2018.util.ext.color
import io.github.droidkaigi.confsched2018.util.ext.observe
import io.github.droidkaigi.confsched2018.util.ext.setLinearDivider
import io.github.droidkaigi.confsched2018.util.ext.toGone
import io.github.droidkaigi.confsched2018.util.ext.toVisible
import timber.log.Timber
import javax.inject.Inject
import kotlin.properties.Delegates

class SearchFragment : DaggerFragment() {
    private lateinit var binding: FragmentSearchBinding
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject lateinit var navigationController: NavigationController
    @Inject lateinit var sessionAlarm: SessionAlarm

    private val sessionsSection = SimpleSessionsSection()
    private val speakersSection = SearchSpeakersSection()

    private val searchViewModel: SearchViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(SearchViewModel::class.java)
    }

    private val onFavoriteClickListener = { session: Session.SpeechSession ->
        searchViewModel.onFavoriteClick(session)
        sessionAlarm.toggleRegister(session)
    }

    private val onFeedbackListener = { session: Session.SpeechSession ->
        navigationController.navigateToSessionsFeedbackActivity(session)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentSearchBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSearchBeforeTabs()
        setupSearch()
    }

    private fun setupSearchBeforeTabs() {
        binding.sessionsViewPager.adapter =
                SearchBeforeViewPagerAdapter(activity!!, childFragmentManager)
        binding.tabLayout.setupWithViewPager(binding.sessionsViewPager)
        binding.tabLayout.addOnTabSelectedListener(
                OnTabReselectedDispatcher(binding.sessionsViewPager)
        )
    }

    private fun setupSearch() {
        setupRecyclerView()
        searchViewModel.result.observe(this, { result ->
            when (result) {
                is Result.Success -> {
                    val searchResult = result.data
                    sessionsSection.updateSessions(
                            searchResult.sessions,
                            onFavoriteClickListener,
                            onFeedbackListener,
                            searchViewModel.searchQuery
                    )
                    speakersSection.updateSpeakers(
                            searchResult.speakers,
                            searchViewModel.searchQuery
                    )
                    binding.sessionsRecycler.scrollToPosition(0)
                }
                is Result.Failure -> {
                    Timber.e(result.e)
                }
            }
        })
    }

    private fun setupRecyclerView() {
        val groupAdapter = GroupAdapter<ViewHolder>().apply {
            add(sessionsSection)
            add(speakersSection)
            setOnItemClickListener({ item, _ ->
                when (item) {
                    is SpeechSessionItem -> {
                        navigationController.navigateToSessionDetailActivity(item.session)
                    }
                    is SearchResultSpeakerItem -> {
                        navigationController.navigateToSpeakerDetailActivity(item.speaker.id)
                    }
                }
            })
        }
        binding.sessionsRecycler.apply {
            adapter = groupAdapter
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            setLinearDivider(R.drawable.shape_divider_vertical_6dp,
                    layoutManager as LinearLayoutManager)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater!!.inflate(R.menu.search, menu)
        val menuSearchItem = menu!!.findItem(R.id.action_search)

        val searchView = menuSearchItem.actionView as SearchView
        searchView.maxWidth = Int.MAX_VALUE

        val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val searchRecentSuggestions = SearchRecentSuggestions(context,
                        SearchSuggestionProvider.AUTHORITY, SearchSuggestionProvider.MODE)
                searchRecentSuggestions.saveRecentQuery(searchViewModel.searchQuery, null)
                searchView.clearFocus()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val query = newText.orEmpty()
                searchViewModel.onQuery(query)
                if (query.isNotBlank()) {
                    binding.searchBeforeGroup.toGone()
                    binding.searchResultGroup.toVisible()
                } else {
                    binding.searchBeforeGroup.toVisible()
                    binding.searchResultGroup.toGone()
                }
                return false
            }
        })

        searchView.setOnSuggestionListener(object : SearchView.OnSuggestionListener {
            override fun onSuggestionSelect(position: Int): Boolean {
                return false
            }

            override fun onSuggestionClick(position: Int): Boolean {
                val item = searchView.suggestionsAdapter.getItem(position) as CrossProcessCursor
                searchView.setQuery(item.getString(2), false)
                return false
            }
        })

        changeSearchViewTextColor(searchView)

        searchView.setOnQueryTextFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
                if (TextUtils.isEmpty(searchView.query)) {
                    searchView.isIconified = true
                } else {
                    val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as
                            InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                }
            }
        }
    }

    private fun changeSearchViewTextColor(view: View) {
        if (view is TextView) {
            view.setTextColor(context!!.color(R.color.app_bar_text_color))
        }

        if (view is ViewGroup) {
            view.forEach {
                changeSearchViewTextColor(it)
            }
        }
    }

    companion object {
        fun newInstance(): SearchFragment = SearchFragment()
    }
}

class SearchBeforeViewPagerAdapter(
        private val activity: Activity,
        fragmentManager: FragmentManager
) : FragmentStateNullablePagerAdapter(fragmentManager) {

    private val fireBaseAnalytics = FirebaseAnalytics.getInstance(activity)
    private var currentFragment by Delegates.observable<Fragment?>(null) { _, old, new ->
        if (old != new && new != null) {
            fireBaseAnalytics.setCurrentScreen(activity, null, new::class.java.simpleName)
        }
    }

    enum class Tab(@StringRes val title: Int) {
        SESSION(R.string.search_before_tab_session) {
            override val fragment: Fragment
                get() = SearchSessionsFragment.newInstance()
        },
        TOPIC(R.string.search_before_tab_topic) {
            override val fragment: Fragment
                get() = SearchTopicsFragment.newInstance()
        },
        SPEAKERS(R.string.search_before_tab_speaker) {
            override val fragment: Fragment
                get() = SearchSpeakersFragment.newInstance()
        };

        abstract val fragment: Fragment
    }

    override fun getPageTitle(position: Int): CharSequence =
            activity.getString(Tab.values()[position].title)

    override fun getItem(position: Int): Fragment = Tab.values()[position].fragment

    override fun getCount(): Int = Tab.values().size

    override fun setPrimaryItem(container: ViewGroup, position: Int, o: Any?) {
        super.setPrimaryItem(container, position, o)
        currentFragment = o as? Fragment
    }
}
