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
import io.github.droidkaigi.confsched2018.databinding.FragmentSearchSpeakersBinding
import io.github.droidkaigi.confsched2018.di.Injectable
import io.github.droidkaigi.confsched2018.presentation.NavigationController
import javax.inject.Inject

class SearchSpeakersFragment : Fragment(), Injectable {
    private lateinit var binding: FragmentSearchSpeakersBinding
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject lateinit var navigationController: NavigationController
    private val searchTopicsViewModel: SearchTopicsViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(SearchTopicsViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentSearchSpeakersBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        // TODO: searchTopicsViewModel.speakers fetch data here
        lifecycle.addObserver(searchTopicsViewModel)
    }

    private fun setupRecyclerView() {
        val groupAdapter = GroupAdapter<ViewHolder>().apply {
            // TODO: Add group and click listener
//            add(speakersGroup)
        }
        binding.searchSessionRecycler.apply {
            adapter = groupAdapter
        }
    }

    companion object {
        fun newInstance(): SearchSpeakersFragment = SearchSpeakersFragment()
    }
}
