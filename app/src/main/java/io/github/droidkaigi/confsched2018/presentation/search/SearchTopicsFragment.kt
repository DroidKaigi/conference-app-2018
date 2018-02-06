package io.github.droidkaigi.confsched2018.presentation.search

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import dagger.android.support.DaggerFragment
import io.github.droidkaigi.confsched2018.databinding.FragmentSearchTopicsBinding
import io.github.droidkaigi.confsched2018.presentation.NavigationController
import io.github.droidkaigi.confsched2018.presentation.Result
import io.github.droidkaigi.confsched2018.presentation.common.view.OnTabReselectedListener
import io.github.droidkaigi.confsched2018.presentation.search.item.TopicItem
import io.github.droidkaigi.confsched2018.presentation.search.item.TopicsGroup
import io.github.droidkaigi.confsched2018.util.ext.observe
import timber.log.Timber
import javax.inject.Inject

class SearchTopicsFragment : DaggerFragment(), OnTabReselectedListener {

    private lateinit var binding: FragmentSearchTopicsBinding
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject lateinit var navigationController: NavigationController
    private val searchTopicsViewModel: SearchTopicsViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(SearchTopicsViewModel::class.java)
    }

    private val topicsGroup = TopicsGroup()

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
        searchTopicsViewModel.topics.observe(this, { result ->
            when (result) {
                is Result.Success -> {
                    topicsGroup.updateTopics(result.data)
                }
                is Result.Failure -> {
                    Timber.e(result.e)
                }
            }
        })
    }

    private fun setupRecyclerView() {
        val groupAdapter = GroupAdapter<ViewHolder>().apply {
            setOnItemClickListener { item, _ ->
                val topicItem = item as? TopicItem ?: return@setOnItemClickListener
                navigationController.navigateToTopicDetailActivity(topicItem.topic.id)
            }
            add(topicsGroup)
        }
        val linearLayoutManager = LinearLayoutManager(context)
        binding.searchSessionRecycler.apply {
            layoutManager = linearLayoutManager
            adapter = groupAdapter
            addItemDecoration(DividerItemDecoration(context, linearLayoutManager.orientation))
        }
    }

    override fun onTabReselected() {
        binding.searchSessionRecycler.smoothScrollToPosition(0)
    }

    companion object {
        fun newInstance(): SearchTopicsFragment = SearchTopicsFragment()
    }
}
