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
import io.github.droidkaigi.confsched2018.databinding.FragmentSearchTopicsBinding
import io.github.droidkaigi.confsched2018.di.Injectable
import io.github.droidkaigi.confsched2018.model.Session
import io.github.droidkaigi.confsched2018.presentation.NavigationController
import javax.inject.Inject

class SearchTopicsFragment : Fragment(), Injectable {
    private lateinit var binding: FragmentSearchTopicsBinding
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject lateinit var navigationController: NavigationController
    private val searchTopicsViewModel: SearchTopicsViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(SearchTopicsViewModel::class.java)
    }

    private val onFavoriteClickListener = { session: Session ->
        // Since it takes time to change the favorite state, change only the state of View first
        session.isFavorited = !session.isFavorited
        binding.searchSessionRecycler.adapter.notifyDataSetChanged()

        searchTopicsViewModel.onFavoriteClick(session)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentSearchTopicsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        // TODO: searchTopicsViewModel.sessions fetch data here
        lifecycle.addObserver(searchTopicsViewModel)
    }

    private fun setupRecyclerView() {
        val groupAdapter = GroupAdapter<ViewHolder>().apply {
            // TODO: Add group and click listener
//            add(sessionsGroup)
        }
        binding.searchSessionRecycler.apply {
            adapter = groupAdapter
        }
    }

    companion object {
        fun newInstance(): SearchTopicsFragment = SearchTopicsFragment()
    }
}

