package io.github.droidkaigi.confsched2018.presentation.search

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import dagger.android.support.DaggerFragment
import io.github.droidkaigi.confsched2018.databinding.FragmentSearchSessionsBinding
import io.github.droidkaigi.confsched2018.model.Session
import io.github.droidkaigi.confsched2018.presentation.NavigationController
import io.github.droidkaigi.confsched2018.presentation.Result
import io.github.droidkaigi.confsched2018.presentation.common.view.OnTabReselectedListener
import io.github.droidkaigi.confsched2018.presentation.search.item.HorizontalSessionItem
import io.github.droidkaigi.confsched2018.presentation.search.item.LevelSessionsSection
import io.github.droidkaigi.confsched2018.util.SessionAlarm
import io.github.droidkaigi.confsched2018.util.ext.observe
import timber.log.Timber
import javax.inject.Inject

class SearchSessionsFragment : DaggerFragment(), OnTabReselectedListener {

    private lateinit var binding: FragmentSearchSessionsBinding
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    private val sessionsSection = LevelSessionsSection(this)

    @Inject lateinit var navigationController: NavigationController
    @Inject lateinit var sessionAlarm: SessionAlarm
    private val searchSessionsViewModel: SearchSessionsViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(SearchSessionsViewModel::class.java)
    }

    private val onFavoriteClickListener = { session: Session.SpeechSession ->
        searchSessionsViewModel.onFavoriteClick(session)
        sessionAlarm.toggleRegister(session)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentSearchSessionsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        searchSessionsViewModel.levelSessions.observe(this, { result ->
            when (result) {
                is Result.Success -> {
                    val levelSessions = result.data
                    sessionsSection.updateSessions(levelSessions, onFavoriteClickListener)
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
            setOnItemClickListener({ item, _ ->
                val sessionItem = (item as? HorizontalSessionItem) ?: return@setOnItemClickListener
                navigationController.navigateToSessionDetailActivity(sessionItem.session)
            })
        }
        binding.searchSessionRecycler.apply {
            adapter = groupAdapter
        }
    }

    override fun onTabReselected() {
        binding.searchSessionRecycler.smoothScrollToPosition(0)
    }

    companion object {
        fun newInstance(): SearchSessionsFragment = SearchSessionsFragment()
    }
}
