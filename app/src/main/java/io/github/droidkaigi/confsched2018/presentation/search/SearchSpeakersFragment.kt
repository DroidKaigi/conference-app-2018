package io.github.droidkaigi.confsched2018.presentation.search

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.analytics.FirebaseAnalytics
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import io.github.droidkaigi.confsched2018.databinding.FragmentSearchSpeakersBinding
import io.github.droidkaigi.confsched2018.di.Injectable
import io.github.droidkaigi.confsched2018.presentation.NavigationController
import io.github.droidkaigi.confsched2018.presentation.Result
import io.github.droidkaigi.confsched2018.presentation.common.itemdecoration.StickyHeaderItemDecoration
import io.github.droidkaigi.confsched2018.presentation.search.item.SpeakerItem
import io.github.droidkaigi.confsched2018.presentation.search.item.SpeakersSection
import io.github.droidkaigi.confsched2018.util.ext.observe
import timber.log.Timber
import javax.inject.Inject

class SearchSpeakersFragment : Fragment(), Injectable {

    private var fireBaseAnalytics: FirebaseAnalytics? = null
    private lateinit var binding: FragmentSearchSpeakersBinding
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject lateinit var navigationController: NavigationController
    private val searchSpeakersViewModel: SearchSpeakersViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(SearchSpeakersViewModel::class.java)
    }

    private val speakersSection = SpeakersSection()

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
        searchSpeakersViewModel.speakers.observe(this, { result ->
            when (result) {
                is Result.Success -> {
                    speakersSection.updateSpeakers(result.data)
                }
                is Result.Failure -> {
                    Timber.e(result.e)
                }
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        fireBaseAnalytics = FirebaseAnalytics.getInstance(context)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            fireBaseAnalytics?.setCurrentScreen(activity!!, null, this::class.java.simpleName)
        }
    }

    private fun setupRecyclerView() {
        val groupAdapter = GroupAdapter<ViewHolder>().apply {
            setOnItemClickListener { item, _ ->
                val speakerItem = item as? SpeakerItem ?: return@setOnItemClickListener
                navigationController.navigateToSpeakerDetailActivity(speakerItem.speaker.id)
            }
            add(speakersSection)
        }
        binding.searchSessionRecycler.apply {
            adapter = groupAdapter
        }

        binding.searchSessionRecycler.addItemDecoration(StickyHeaderItemDecoration(context,
                object : StickyHeaderItemDecoration.Callback {
                    override fun getGroupId(position: Int): Long {
                        val initial = speakersSection.getSpeakerNameOrNull(position)?.get(0)
                        initial ?: return -1
                        return Character.toUpperCase(initial).toLong()
                    }

                    override fun getGroupFirstLine(position: Int): String? {
                        return speakersSection.getSpeakerNameOrNull(position)?.get(0)?.toString()
                    }
                }))
    }

    companion object {
        fun newInstance(): SearchSpeakersFragment = SearchSpeakersFragment()
    }
}
