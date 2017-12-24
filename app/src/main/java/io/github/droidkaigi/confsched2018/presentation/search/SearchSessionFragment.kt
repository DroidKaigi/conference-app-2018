package io.github.droidkaigi.confsched2018.presentation.search

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import io.github.droidkaigi.confsched2018.databinding.FragmentSearchSessionBinding
import io.github.droidkaigi.confsched2018.di.Injectable
import io.github.droidkaigi.confsched2018.model.Session
import io.github.droidkaigi.confsched2018.presentation.Result
import io.github.droidkaigi.confsched2018.presentation.search.item.LevelSessionsGroup
import io.github.droidkaigi.confsched2018.util.ext.observe
import timber.log.Timber
import javax.inject.Inject

class SearchSessionFragment : Fragment(), Injectable {
    private lateinit var binding: FragmentSearchSessionBinding
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    private val sessionsGroup = LevelSessionsGroup(this)

    private val searchSessionViewModel: SearchSessionViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(SearchSessionViewModel::class.java)
    }

    private val onFavoriteClickListener = { session: Session ->
        // Since it takes time to change the favorite state, change only the state of View first
        session.isFavorited = !session.isFavorited
        binding.searchSessionRecycler.adapter.notifyDataSetChanged()

        searchSessionViewModel.onFavoriteClick(session)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentSearchSessionBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSearch()
    }

    private fun setupSearch() {
        setupRecyclerView()
        searchSessionViewModel.levelSessions.observe(this, { result ->
            when (result) {
                is Result.Success -> {
                    val levelSessions = result.data
                    sessionsGroup.updateSessions(levelSessions, onFavoriteClickListener)
                }
                is Result.Failure -> {
                    Timber.e(result.e)
                }
            }
        })
        lifecycle.addObserver(searchSessionViewModel)
    }

    private fun setupRecyclerView() {
        val groupAdapter = GroupAdapter<ViewHolder>().apply {
            add(sessionsGroup)
            setOnItemClickListener({ _, _ ->
                //TODO
            })
        }
        binding.searchSessionRecycler.apply {
            adapter = groupAdapter
        }

    }

    companion object {
        fun newInstance(): SearchSessionFragment = SearchSessionFragment()
    }
}

