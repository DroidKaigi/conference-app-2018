package io.github.droidkaigi.confsched2018.presentation.contributor

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.SimpleItemAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import dagger.android.support.DaggerFragment
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.databinding.FragmentContributorBinding
import io.github.droidkaigi.confsched2018.presentation.NavigationController
import io.github.droidkaigi.confsched2018.presentation.Result
import io.github.droidkaigi.confsched2018.presentation.contributor.item.ContributorItem
import io.github.droidkaigi.confsched2018.presentation.contributor.item.ContributorsSection
import io.github.droidkaigi.confsched2018.util.ext.observe
import timber.log.Timber
import javax.inject.Inject

class ContributorsFragment : DaggerFragment() {

    private lateinit var binding: FragmentContributorBinding
    private val contributorsViewModel: ContributorsViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(ContributorsViewModel::class.java)
    }
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject lateinit var navigationController: NavigationController
    private val contributorSection = ContributorsSection()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentContributorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupRecyclerView()
        setupSwipeRefresh()
        contributorsViewModel.contributors.observe(this, { result ->
            when (result) {
                is Result.Success -> {
                    val contributors = result.data
                    contributorSection.updateContributors(contributors)
                }
                is Result.Failure -> {
                    Timber.e(result.e)
                }
            }
        })
        lifecycle.addObserver(contributorsViewModel)
    }

    private fun setupRecyclerView() {
        val groupAdapter = GroupAdapter<ViewHolder>().apply {
            add(contributorSection)
            setOnItemClickListener({ item, _ ->
                if (item !is ContributorItem) {
                    return@setOnItemClickListener
                }
                navigationController.navigateToExternalBrowser(item.contributor.htmlUrl)
            })
        }
        binding.contributorsRecycler.apply {
            adapter = groupAdapter
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        }
    }

    private fun setupSwipeRefresh() {
        binding.contributorsSwipeRefresh.apply {
            setColorSchemeResources(R.color.accent)
            setOnRefreshListener({
                contributorsViewModel.onRefreshContributors()

                if (binding.contributorsSwipeRefresh.isRefreshing) {
                    binding.contributorsSwipeRefresh.isRefreshing = false
                }
            })
        }
    }

    companion object {
        fun newInstance(): ContributorsFragment = ContributorsFragment()
    }
}
