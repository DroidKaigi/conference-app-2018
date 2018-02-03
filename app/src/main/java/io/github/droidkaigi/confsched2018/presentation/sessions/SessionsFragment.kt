package io.github.droidkaigi.confsched2018.presentation.sessions

import android.app.Activity
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.analytics.FirebaseAnalytics
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.databinding.FragmentSessionsBinding
import io.github.droidkaigi.confsched2018.di.Injectable
import io.github.droidkaigi.confsched2018.model.Room
import io.github.droidkaigi.confsched2018.presentation.FragmentStateNullablePagerAdapter
import io.github.droidkaigi.confsched2018.presentation.MainActivity
import io.github.droidkaigi.confsched2018.presentation.MainActivity.BottomNavigationItem.OnReselectedListener
import io.github.droidkaigi.confsched2018.presentation.Result
import io.github.droidkaigi.confsched2018.presentation.common.fragment.Findable
import io.github.droidkaigi.confsched2018.presentation.common.view.OnTabReselectedListener
import io.github.droidkaigi.confsched2018.util.ProgressTimeLatch
import io.github.droidkaigi.confsched2018.util.ext.observe
import timber.log.Timber
import javax.inject.Inject
import kotlin.properties.Delegates

class SessionsFragment : Fragment(), Injectable, Findable, OnReselectedListener {
    private lateinit var binding: FragmentSessionsBinding
    private lateinit var sessionsViewPagerAdapter: SessionsViewPagerAdapter
    private lateinit var sessionsViewModel: SessionsViewModel
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentSessionsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionsViewPagerAdapter = SessionsViewPagerAdapter(childFragmentManager, activity!!)
        binding.sessionsViewPager.adapter = sessionsViewPagerAdapter

        sessionsViewModel = ViewModelProviders
                .of(this, viewModelFactory)
                .get(SessionsViewModel::class.java)

        val progressTimeLatch = ProgressTimeLatch {
            binding.progress.visibility = if (it) View.VISIBLE else View.GONE
        }
        sessionsViewModel.rooms.observe(this, { result ->
            when (result) {
                is Result.Success -> {
                    sessionsViewPagerAdapter.setRooms(result.data)
                }
                is Result.Failure -> {
                    Timber.e(result.e)
                }
            }
        })
        sessionsViewModel.isLoading.observe(this, { isLoading ->
            progressTimeLatch.loading = isLoading ?: false
        })
        sessionsViewModel.refreshResult.observe(this, { result ->
            when (result) {
                is Result.Failure -> {
                    // If user is offline, not error. So we write log to debug
                    Timber.d(result.e)
                    Snackbar.make(view, R.string.session_fetch_failed, Snackbar.LENGTH_LONG).apply {
                        setAction(R.string.session_load_retry) {
                            sessionsViewModel.onRetrySessions()
                        }
                    }.show()
                }
            }
        })

        lifecycle.addObserver(sessionsViewModel)

        binding.tabLayout.setupWithViewPager(binding.sessionsViewPager)
        binding.tabLayout.addOnTabSelectedListener(
                OnTabReselectedListener(binding.sessionsViewPager))
    }

    override fun onReselected() {
        val currentItem = binding.sessionsViewPager.currentItem
        val fragment = sessionsViewPagerAdapter
                .instantiateItem(binding.sessionsViewPager, currentItem)
        if (fragment is CurrentSessionScroller) {
            fragment.scrollToCurrentSession()
        }
    }

    override val tagForFinding = MainActivity.BottomNavigationItem.SESSION.name

    interface CurrentSessionScroller {
        fun scrollToCurrentSession()
    }

    companion object {
        fun newInstance(): SessionsFragment = SessionsFragment()
    }
}

class SessionsViewPagerAdapter(
        fragmentManager: FragmentManager,
        private val activity: Activity
) : FragmentStateNullablePagerAdapter(fragmentManager) {

    private val fireBaseAnalytics = FirebaseAnalytics.getInstance(activity)
    private var currentTab by Delegates.observable<Tab?>(null) { _, old, new ->
        if (old != new && new != null) {
            fireBaseAnalytics.setCurrentScreen(activity, null, new.screenName)
        }
    }

    private val tabs = arrayListOf<Tab>()
    private var roomTabs = mutableListOf<Tab.RoomTab>()

    sealed class Tab(val title: String, val fragment: Fragment, val screenName: String) {
        object All : Tab("All", AllSessionsFragment.newInstance(),
                AllSessionsFragment::class.java.simpleName)

        data class RoomTab(val room: Room) : Tab(room.name, RoomSessionsFragment.newInstance(room),
                RoomSessionsFragment::class.java.simpleName + room.name)
    }

    private fun setupTabs() {
        tabs.clear()
        tabs.add(Tab.All)
        tabs.addAll(roomTabs)
        notifyDataSetChanged()
    }

    override fun getPageTitle(position: Int): CharSequence = tabs[position].title

    override fun getItem(position: Int): Fragment = tabs[position].fragment

    override fun getCount(): Int = tabs.size

    override fun setPrimaryItem(container: ViewGroup, position: Int, o: Any?) {
        super.setPrimaryItem(container, position, o)
        currentTab = tabs.getOrNull(position)
    }

    fun setRooms(rooms: List<Room>) {
        if (rooms == roomTabs.map { it.room }) {
            return
        }
        roomTabs = rooms.map {
            Tab.RoomTab(it)
        }.toMutableList()
        setupTabs()
    }
}
