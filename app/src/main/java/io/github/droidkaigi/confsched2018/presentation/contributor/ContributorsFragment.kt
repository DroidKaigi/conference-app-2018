package io.github.droidkaigi.confsched2018.presentation.contributor

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.net.Uri
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.SimpleItemAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.databinding.FragmentContributorBinding
import io.github.droidkaigi.confsched2018.di.Injectable
import io.github.droidkaigi.confsched2018.presentation.NavigationController
import io.github.droidkaigi.confsched2018.presentation.Result
import io.github.droidkaigi.confsched2018.presentation.common.binding.FragmentDataBindingComponent
import io.github.droidkaigi.confsched2018.presentation.contributor.item.ContributorHeaderItem
import io.github.droidkaigi.confsched2018.presentation.contributor.item.ContributorItem
import io.github.droidkaigi.confsched2018.presentation.contributor.item.ContributorsSection
import io.github.droidkaigi.confsched2018.util.ext.observe
import timber.log.Timber
import javax.inject.Inject

class ContributorsFragment : Fragment(), Injectable {

    private lateinit var binding: FragmentContributorBinding
    private val contributorsViewModel: ContributorsViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(ContributorsViewModel::class.java)
    }
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject lateinit var navigationController: NavigationController
    private val fragmentDataBindingComponent = FragmentDataBindingComponent(this)
    private val contributorSection = ContributorsSection(fragmentDataBindingComponent)

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
                    val header =
                            ContributorHeaderItem(contributors.size, fragmentDataBindingComponent)
                    contributorSection.setHeader(header)
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
            setOnItemClickListener({ item, view ->
                //TODO Replace this with nav controller after #176 merged.
                //https://github.com/DroidKaigi/conference-app-2018/pull/176/
                if (item is ContributorItem) {
                    val url = item.contributor.htmlUrl
                    val intent = CustomTabsIntent.Builder()
                            .setShowTitle(true)
                            .setToolbarColor(ContextCompat.getColor(view.context, R.color.primary))
                            .build()
                    intent.launchUrl(activity, Uri.parse(url))
                }
            })
        }
        binding.contributorsRecycler.apply {
            adapter = groupAdapter
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        }
    }

    private fun setupSwipeRefresh() {
        binding.contributorsSwipeRefresh.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            contributorsViewModel.onRefreshContributors()

            if (binding.contributorsSwipeRefresh.isRefreshing()) {
                binding.contributorsSwipeRefresh.setRefreshing(false)
            }
        })
    }

    companion object {
        fun newInstance(): ContributorsFragment = ContributorsFragment()
    }
}
