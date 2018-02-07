package io.github.droidkaigi.confsched2018.presentation.sessions

import android.app.Activity
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.database.DataSetObserver
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.android.support.DaggerFragment
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.databinding.FragmentSessionsBinding
import io.github.droidkaigi.confsched2018.model.Room
import io.github.droidkaigi.confsched2018.model.SessionSchedule
import io.github.droidkaigi.confsched2018.presentation.FragmentStateNullablePagerAdapter
import io.github.droidkaigi.confsched2018.presentation.MainActivity
import io.github.droidkaigi.confsched2018.presentation.MainActivity.BottomNavigationItem.OnReselectedListener
import io.github.droidkaigi.confsched2018.presentation.Result
import io.github.droidkaigi.confsched2018.presentation.common.fragment.Findable
import io.github.droidkaigi.confsched2018.presentation.common.pref.Prefs
import io.github.droidkaigi.confsched2018.presentation.common.pref.PreviousSessionPrefs
import io.github.droidkaigi.confsched2018.presentation.common.view.OnTabReselectedDispatcher
import io.github.droidkaigi.confsched2018.util.ProgressTimeLatch
import io.github.droidkaigi.confsched2018.util.ext.observe
import io.github.droidkaigi.confsched2018.util.ext.toReadableTimeString
import timber.log.Timber
import java.util.Date
import javax.inject.Inject
import kotlin.properties.Delegates

class SessionsFragment : DaggerFragment(), Findable, OnReselectedListener {
    private lateinit var binding: FragmentSessionsBinding
    private lateinit var sessionsViewPagerAdapter: SessionsViewPagerAdapter
    private lateinit var sessionsViewModel: SessionsViewModel
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.sessions, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val roomTabMenu = menu.findItem(R.id.room_tab_mode)
        val scheduleTabMenu = menu.findItem(R.id.schedule_tab_mode)

        Timber.d("onPrepareOptionsMenu")

        when (sessionsViewModel.tabMode) {
            SessionTabMode.SCHEDULE -> {
                roomTabMenu.isVisible = false
                scheduleTabMenu.isVisible = true
                scheduleTabMenu.isEnabled = true
            }
            SessionTabMode.ROOM -> {
                scheduleTabMenu.isVisible = false
                roomTabMenu.isVisible = true
                roomTabMenu.isEnabled = true
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.room_tab_mode -> true.apply {
                item.isEnabled = false
                sessionsViewModel.changeTabMode(SessionTabMode.SCHEDULE)
            }
            R.id.schedule_tab_mode -> true.apply {
                item.isEnabled = false
                sessionsViewModel.changeTabMode(SessionTabMode.ROOM)
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentSessionsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionsViewPagerAdapter = SessionsViewPagerAdapter(childFragmentManager, activity!!)
        sessionsViewPagerAdapter.registerDataSetObserver(object : DataSetObserver() {
            override fun onChanged() {
                binding.tabLayout.setScrollPosition(0, 0f, true)
                binding.sessionsViewPager.currentItem = 0
            }
        })
        binding.sessionsViewPager.adapter = sessionsViewPagerAdapter
        binding.sessionsViewPager.pageMargin =
                resources.getDimensionPixelSize(R.dimen.session_page_margin)

        sessionsViewModel = ViewModelProviders
                .of(this, viewModelFactory)
                .get(SessionsViewModel::class.java)

        if (Prefs.enableReopenPreviousRoomSessions and (savedInstanceState == null)) {
            sessionsViewModel.changeTabMode(PreviousSessionPrefs.previousSessionTabMode)
        }

        val progressTimeLatch = ProgressTimeLatch {
            binding.progress.visibility = if (it) View.VISIBLE else View.GONE
        }
        sessionsViewModel.tab.observe(this, { result ->
            when (result) {
                is Result.Success -> {
                    sessionsViewPagerAdapter.setSessionTab(result.data)
                    activity?.invalidateOptionsMenu()
                    if ((result.data is SessionTab.Room) and
                            Prefs.enableReopenPreviousRoomSessions and
                            (savedInstanceState == null)) {
                        reopenPreviousOpenedItem()
                    }
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
                OnTabReselectedDispatcher(binding.sessionsViewPager)
        )
        sessionsViewPagerAdapter.openedScheduleTabObserver = {
            scrollToRecentScheduleTab()
        }
    }

    override fun onPause() {
        super.onPause()
        when (Prefs.enableReopenPreviousRoomSessions) {
            true -> saveCurrentSession()
            false -> PreviousSessionPrefs.initPreviousSessionPrefs()
        }
    }

    private fun saveCurrentSession() {
        val currentItem = binding.sessionsViewPager.currentItem
        if (sessionsViewPagerAdapter.count <= currentItem) return

        PreviousSessionPrefs.previousSessionTabIndex = currentItem
        PreviousSessionPrefs.previousSessionTabMode = sessionsViewModel.tabMode
        val fragment = sessionsViewPagerAdapter
                .instantiateItem(binding.sessionsViewPager, currentItem)
        if (fragment is SavePreviousSessionScroller) {
            fragment.requestSavingScrollState()
        }
    }

    override fun onReselected() {
        when (sessionsViewModel.tabMode) {
            SessionTabMode.ROOM -> {
                val currentItem = binding.sessionsViewPager.currentItem
                if (sessionsViewPagerAdapter.count <= currentItem) return

                val fragment = sessionsViewPagerAdapter
                        .instantiateItem(binding.sessionsViewPager, currentItem)

                if (fragment is CurrentSessionScroller) {
                    fragment.scrollToCurrentSession()
                }
            }
            SessionTabMode.SCHEDULE -> {
                scrollToRecentScheduleTab()
            }
        }
    }

    private fun scrollToRecentScheduleTab() {
        val position = sessionsViewPagerAdapter.getRecentScheduleTabPosition()
        binding.sessionsViewPager.currentItem = position
    }

    private fun reopenPreviousOpenedItem() {
        val previousItem = PreviousSessionPrefs.previousSessionTabIndex
        if (previousItem < 0) return
        if (sessionsViewPagerAdapter.count <= previousItem) return

        binding.sessionsViewPager.currentItem = previousItem
        val fragment = sessionsViewPagerAdapter
                .instantiateItem(binding.sessionsViewPager, previousItem)
        if (fragment is SavePreviousSessionScroller) {
            fragment.requestRestoringScrollState()
        }
    }

    override val tagForFinding = MainActivity.BottomNavigationItem.SESSION.name

    interface CurrentSessionScroller {
        fun scrollToCurrentSession()
    }

    interface SavePreviousSessionScroller {
        fun requestSavingScrollState()
        fun requestRestoringScrollState()
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
    private var schedulesTabs = mutableListOf<Tab.TimeTab>()
    var openedScheduleTabObserver: (() -> Unit)? = null

    sealed class Tab(val title: String) {
        abstract val fragment: Fragment
        abstract val screenName: String

        object All : Tab("All") {
            override val fragment: Fragment
                get() = AllSessionsFragment.newInstance()
            override val screenName: String =
                    AllSessionsFragment::class.java.simpleName
        }

        data class RoomTab(val room: Room) : Tab(room.name) {
            override val fragment: Fragment
                get() = RoomSessionsFragment.newInstance(room)
            override val screenName: String =
                    RoomSessionsFragment::class.java.simpleName + room.name
        }

        data class TimeTab(val schedule: SessionSchedule) :
                Tab("Day${schedule.dayNumber} / ${schedule.startTime.toReadableTimeString()}") {
            override val fragment: Fragment
                get() = ScheduleSessionsFragment.newInstance(schedule)
            override val screenName: String =
                    ScheduleSessionsFragment::class.java.simpleName + title
        }
    }

    private fun setupTabsIfNeeded(otherTabs: List<Tab>) {
        if (tabs.isNotEmpty() && tabs.subList(1, tabs.size) == otherTabs) {
            return
        }

        tabs.clear()
        tabs.add(Tab.All)
        tabs.addAll(otherTabs)
        notifyDataSetChanged()
        if (otherTabs.first() is Tab.TimeTab) {
            openedScheduleTabObserver?.invoke()
        }
    }

    override fun getPageTitle(position: Int): CharSequence = tabs[position].title

    override fun getItem(position: Int): Fragment = tabs[position].fragment

    override fun getItemPosition(`object`: Any): Int {
        // For recreating Page
        return PagerAdapter.POSITION_NONE
    }

    override fun getCount(): Int = tabs.size

    override fun setPrimaryItem(container: ViewGroup, position: Int, o: Any?) {
        super.setPrimaryItem(container, position, o)
        currentTab = tabs.getOrNull(position)
    }

    fun getRecentScheduleTabPosition(time: Date = Date()): Int {
        val position = schedulesTabs.withIndex().firstOrNull {
            it.value.schedule.startTime > time
        }?.index?.dec() ?: 0

        return position + 1
    }

    fun setSessionTab(tab: SessionTab) {
        when (tab) {
            is SessionTab.Room -> {
                setRooms(tab.stuffs)
            }
            is SessionTab.Schedule -> {
                setSchedules(tab.stuffs)
            }
        }
    }

    private fun setRooms(rooms: List<Room>) {
        if (rooms != roomTabs.map { it.room }) {
            roomTabs = rooms.map {
                Tab.RoomTab(it)
            }.toMutableList()
        }

        setupTabsIfNeeded(roomTabs)
    }

    private fun setSchedules(schedules: List<SessionSchedule>) {
        if (schedules != schedulesTabs.map { it.schedule }) {
            schedulesTabs = schedules.map {
                Tab.TimeTab(it)
            }.toMutableList()
        }

        setupTabsIfNeeded(schedulesTabs)
    }
}
