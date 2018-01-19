package io.github.droidkaigi.confsched2018.presentation.sponsors

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import io.github.droidkaigi.confsched2018.databinding.FragmentSponsorsBinding
import io.github.droidkaigi.confsched2018.di.Injectable
import io.github.droidkaigi.confsched2018.model.SponsorPlan
import io.github.droidkaigi.confsched2018.presentation.NavigationController
import io.github.droidkaigi.confsched2018.presentation.Result
import io.github.droidkaigi.confsched2018.presentation.sponsors.item.SponsorItem
import io.github.droidkaigi.confsched2018.util.ProgressTimeLatch
import io.github.droidkaigi.confsched2018.util.ext.observe
import timber.log.Timber
import javax.inject.Inject

const val SPONSOR_SCREEN_MAX_SPAN_SIZE = 6

class SponsorsFragment : Fragment(), Injectable {
    private lateinit var binding: FragmentSponsorsBinding
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject lateinit var navigationController: NavigationController

    private val sponsorPlansSection = SponsorsSection()

    private val sponsorsViewModel: SponsorsViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(SponsorsViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentSponsorsBinding.inflate(inflater, container!!, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycle.addObserver(sponsorsViewModel)

        val context = context ?: throw AssertionError("this never happen")

        initRecyclerView(context)

        val progressTimeLatch = ProgressTimeLatch {
            binding.progress.visibility = if (it) View.VISIBLE else View.GONE
        }

        sponsorsViewModel.sponsors.observe(this) {
            when (it) {
                is Result.Success<List<SponsorPlan>> -> {
                    bindSponsorsToRecycler(it.data)
                }
                is Result.Failure -> {
                    Timber.e(it.e)
                }
            }
        }

        sponsorsViewModel.isLoading.observe(this) { isLoading ->
            progressTimeLatch.loading = isLoading ?: false
        }
    }

    override fun onDestroyView() {
        lifecycle.removeObserver(sponsorsViewModel)
        super.onDestroyView()
    }

    private fun initRecyclerView(context: Context) {
        val groupAdapter = GroupAdapter<ViewHolder>().apply {
            spanCount = SPONSOR_SCREEN_MAX_SPAN_SIZE
            add(sponsorPlansSection)
            setOnItemClickListener({ item, _ ->
                val url = (item as? SponsorItem)?.sponsor?.link ?: return@setOnItemClickListener
                navigationController.navigateToExternalBrowser(url)
            })
        }
        val layoutManager = GridLayoutManager(context, SPONSOR_SCREEN_MAX_SPAN_SIZE).apply {
            spanSizeLookup = groupAdapter.spanSizeLookup
        }
        binding.sponsorRecycler.layoutManager = layoutManager
        binding.sponsorRecycler.adapter = groupAdapter
    }

    private fun bindSponsorsToRecycler(sponsorPlans: List<SponsorPlan>) {
        sponsorPlansSection.updateSponsorPlans(this, sponsorPlans)
    }

    companion object {
        fun newInstance(): SponsorsFragment = SponsorsFragment()
    }
}
