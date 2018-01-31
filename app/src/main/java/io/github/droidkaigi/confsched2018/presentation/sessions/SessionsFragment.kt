package io.github.droidkaigi.confsched2018.presentation.sessions

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import io.github.droidkaigi.confsched2018.BuildConfig
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.databinding.FragmentSessionsBinding
import io.github.droidkaigi.confsched2018.di.Injectable
import io.github.droidkaigi.confsched2018.model.Room
import io.github.droidkaigi.confsched2018.model.SessionSchedule
import io.github.droidkaigi.confsched2018.presentation.MainActivity
import io.github.droidkaigi.confsched2018.presentation.MainActivity.BottomNavigationItem.OnReselectedListener
import io.github.droidkaigi.confsched2018.presentation.Result
import io.github.droidkaigi.confsched2018.presentation.common.fragment.Findable
import io.github.droidkaigi.confsched2018.util.ProgressTimeLatch
import io.github.droidkaigi.confsched2018.util.ext.observe
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class SessionsFragment : Fragment(), Injectable, Findable, OnReselectedListener {
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
            is SessionTabMode.ScheduleTabMode -> {
                roomTabMenu.isVisible = false
                scheduleTabMenu.isVisible = true
                scheduleTabMenu.isEnabled = true
            }
            is SessionTabMode.RoomTabMode -> {
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
                sessionsViewModel.changeTabMode(SessionTabMode.ScheduleTabMode)
            }
            R.id.schedule_tab_mode -> true.apply {
                item.isEnabled = false
                sessionsViewModel.changeTabMode(SessionTabMode.RoomTabMode)
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

        sessionsViewPagerAdapter = SessionsViewPagerAdapter(childFragmentManager)
        binding.sessionsViewPager.adapter = sessionsViewPagerAdapter

        sessionsViewModel = ViewModelProviders
                .of(this, viewModelFactory)
                .get(SessionsViewModel::class.java)

        val progressTimeLatch = ProgressTimeLatch {
            binding.progress.visibility = if (it) View.VISIBLE else View.GONE
        }
        sessionsViewModel.tabStuffs.observe(this, { result ->
            when (result) {
                is Result.Success -> {
                    sessionsViewPagerAdapter.setTabStuffs(result.data)
                    activity?.invalidateOptionsMenu()
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
    }

    override fun onReselected() {
        when (sessionsViewModel.tabMode) {
            is SessionTabMode.RoomTabMode -> {
                val currentItem = binding.sessionsViewPager.currentItem
                val fragment = sessionsViewPagerAdapter
                        .instantiateItem(binding.sessionsViewPager, currentItem)

                if (fragment is CurrentSessionScroller) {
                    fragment.scrollToCurrentSession()
                }
            }
            is SessionTabMode.ScheduleTabMode -> {
                val now = Date(ZonedDateTime.now(ZoneId.of(ZoneId.SHORT_IDS["JST"]))
                        .toInstant().toEpochMilli())
                val position = sessionsViewPagerAdapter.getRecentScheduleTabPosition(now)
                binding.sessionsViewPager.currentItem = position
            }
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
        fragmentManager: FragmentManager
) : FragmentStatePagerAdapter(fragmentManager) {
    companion object {
        private val startDateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    }

    private val tabs = arrayListOf<Tab>()
    private var roomTabs = mutableListOf<Tab.RoomTab>()
    private var schedulesTabs = mutableListOf<Tab.TimeTab>()

    sealed class Tab(val title: String) {
        object All : Tab("All")
        data class RoomTab(val room: Room) : Tab(room.name)
        data class TimeTab(val schedule: SessionSchedule) :
                Tab("Day${schedule.dayNumber} / ${startDateFormat.format(schedule.startTime)}")
    }

    private fun setupTabs(otherTabs: List<Tab>) {
        tabs.clear()
        tabs.add(Tab.All)
        tabs.addAll(otherTabs)
        notifyDataSetChanged()
    }

    override fun getPageTitle(position: Int): CharSequence = tabs[position].title

    override fun getItem(position: Int): Fragment {
        val tab = tabs[position]
        return when (tab) {
            Tab.All -> {
                AllSessionsFragment.newInstance()
            }
            is Tab.RoomTab -> {
                RoomSessionsFragment.newInstance(tab.room)
            }
            is Tab.TimeTab -> {
                ScheduleSessionsFragment.newInstance(tab.schedule)
            }
        }
    }

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

    override fun getCount(): Int = tabs.size

    fun getRecentScheduleTabPosition(time: Date): Int {
        val position = schedulesTabs.withIndex().firstOrNull {
            it.value.schedule.startTime > time
        }?.index?.dec() ?: 0

        return position + 1
    }

    fun setTabStuffs(tabStuffs: List<Any>) {
        val sample = tabStuffs.firstOrNull()

        when (sample) {
            is Room -> {
                if (BuildConfig.DEBUG) {
                    if (!tabStuffs.all { it is Room }) {
                        throw IllegalStateException("Tab stuffs contain non-Room class")
                    }
                }

                setRooms(tabStuffs as List<Room>)
            }
            is SessionSchedule -> {
                if (BuildConfig.DEBUG) {
                    if (!tabStuffs.all { it is SessionSchedule }) {
                        throw IllegalStateException("Tab stuffs contain non-SessionSchedule class")
                    }
                }

                setSchedules(tabStuffs as List<SessionSchedule>)
            }
            null -> throw IllegalArgumentException("No tab stuff found")
            else -> throw IllegalStateException("Unknown tab stuff was passed : $sample")
        }
    }

    private fun setRooms(rooms: List<Room>) {
        if (rooms != roomTabs.map { it.room }) {
            roomTabs = rooms.map {
                Tab.RoomTab(it)
            }.toMutableList()
        }

        setupTabs(roomTabs)
    }

    private fun setSchedules(schedules: List<SessionSchedule>) {
        if (schedules != schedulesTabs.map { it.schedule }) {
            schedulesTabs = schedules.map {
                Tab.TimeTab(it)
            }.toMutableList()
        }

        setupTabs(schedulesTabs)
    }
}
