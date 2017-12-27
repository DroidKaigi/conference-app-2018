package io.github.droidkaigi.confsched2018.presentation.search

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.constraint.ConstraintSet
import android.support.transition.TransitionInflater
import android.support.transition.TransitionManager
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.support.v7.widget.SimpleItemAnimator
import android.view.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.databinding.FragmentSearchBinding
import io.github.droidkaigi.confsched2018.di.Injectable
import io.github.droidkaigi.confsched2018.model.Session
import io.github.droidkaigi.confsched2018.presentation.Result
import io.github.droidkaigi.confsched2018.presentation.sessions.AllSessionsFragment
import io.github.droidkaigi.confsched2018.presentation.sessions.item.DateSessionsGroup
import io.github.droidkaigi.confsched2018.util.ext.*
import timber.log.Timber
import javax.inject.Inject

class SearchFragment : Fragment(), Injectable {
    private lateinit var binding: FragmentSearchBinding
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    private val sessionsGroup = DateSessionsGroup(this)

    private val searchViewModel: SearchViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(SearchViewModel::class.java)
    }

    private val onFavoriteClickListener = { session: Session ->
        // Since it takes time to change the favorite state, change only the state of View first
        session.isFavorited = !session.isFavorited
        binding.sessionsRecycler.adapter.notifyDataSetChanged()

        searchViewModel.onFavoriteClick(session)
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
        binding.sessionsViewPager.adapter = SearchBeforeViewPagerAdapter(context!!, childFragmentManager)
        binding.tabLayout.setupWithViewPager(binding.sessionsViewPager)
    }

    private fun setupSearch() {
        setupRecyclerView()
        searchViewModel.sessions.observe(this, { result ->
            when (result) {
                is Result.Success -> {
                    val sessions = result.data
                    sessionsGroup.updateSessions(sessions, onFavoriteClickListener)
                    binding.sessionsRecycler.scrollToPosition(0)
                }
                is Result.Failure -> {
                    Timber.e(result.e)
                }
            }
        })
        lifecycle.addObserver(searchViewModel)
    }

    private fun setupRecyclerView() {
        val groupAdapter = GroupAdapter<ViewHolder>().apply {
            add(sessionsGroup)
            setOnItemClickListener({ _, _ ->
                //TODO
            })
        }
        binding.sessionsRecycler.apply {
            adapter = groupAdapter
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

            addOnScrollListener(
                    onScrollStateChanged = { _: RecyclerView?, newState: Int ->
                        if (binding.sessionsRecycler.isGone()) return@addOnScrollListener
                        setDayHeaderVisibility(newState != RecyclerView.SCROLL_STATE_IDLE)
                    },
                    onScrolled = { _, _, _ ->
                        val firstPosition = (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                        val dayNumber = sessionsGroup.getDateCountSinceBeginOrNull(firstPosition)
                        dayNumber ?: return@addOnScrollListener
                        val dayTitle = getString(R.string.session_day_title, dayNumber)
                        binding.dayHeader.setTextIfChanged(dayTitle)
                    })
        }
    }

    private fun setDayHeaderVisibility(visibleDayHeader: Boolean) {
        val transition = TransitionInflater
                .from(context)
                .inflateTransition(R.transition.date_header_visibility)
        TransitionManager.beginDelayedTransition(binding.sessionsConstraintLayout, transition)
        val constraintSet = if (visibleDayHeader) {
            binding.sessionsConstraintLayout
                    .clone()
                    .apply { setVisibility(R.id.day_header, ConstraintSet.VISIBLE) }
        } else {
            binding.sessionsConstraintLayout
                    .clone()
                    .apply { setVisibility(R.id.day_header, ConstraintSet.GONE) }
        }
        constraintSet.applyTo(binding.sessionsConstraintLayout)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater!!.inflate(R.menu.search, menu)
        val menuSearchItem = menu!!.findItem(R.id.action_search)
        val searchView = menuSearchItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

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
    }

    companion object {
        fun newInstance(): SearchFragment = SearchFragment()
    }
}

class SearchBeforeViewPagerAdapter(val context: Context, fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {

    enum class Tab(@StringRes val title: Int) {
        Session(R.string.search_before_tab_session),
        Topic(R.string.search_before_tab_topic),
        User(R.string.search_before_tab_user);
    }

    override fun getPageTitle(position: Int): CharSequence = context.getString(Tab.values()[position].title)

    override fun getItem(position: Int): Fragment {
        val tab = Tab.values()[position]
        return when (tab) {
            Tab.Session -> SearchSessionFragment.newInstance()
            Tab.Topic -> AllSessionsFragment.newInstance()
            Tab.User -> AllSessionsFragment.newInstance()
        }
    }

    override fun getCount(): Int = Tab.values().size

}
