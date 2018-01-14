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
import com.xwray.groupie.Section
import com.xwray.groupie.ViewHolder
import io.github.droidkaigi.confsched2018.databinding.FragmentSponsorsBinding
import io.github.droidkaigi.confsched2018.di.Injectable
import io.github.droidkaigi.confsched2018.model.SponsorPlan
import io.github.droidkaigi.confsched2018.presentation.NavigationController
import io.github.droidkaigi.confsched2018.presentation.Result
import io.github.droidkaigi.confsched2018.presentation.sponsors.item.EmptySponsorItem
import io.github.droidkaigi.confsched2018.presentation.sponsors.item.SponsorItem
import io.github.droidkaigi.confsched2018.presentation.sponsors.item.SponsorPlanItem
import io.github.droidkaigi.confsched2018.presentation.sponsors.item.getSponsorItemSpanSize
import io.github.droidkaigi.confsched2018.util.ext.observe
import timber.log.Timber
import javax.inject.Inject

private const val MAX_SPAN_SIZE = 6

class SponsorsFragment : Fragment(), Injectable {
    private lateinit var binding: FragmentSponsorsBinding
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject lateinit var navigationController: NavigationController

    private val sponsorPlansSection = Section()

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
        val context = context ?: throw AssertionError("this never happen")

        initRecyclerView(context)

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
    }

    fun initRecyclerView(context: Context) {
        val groupAdapter = GroupAdapter<ViewHolder>().apply {
            spanCount = MAX_SPAN_SIZE
            add(sponsorPlansSection)
            setOnItemClickListener({ item, _ ->
                val url = (item as? SponsorItem)?.sponsor?.link ?: return@setOnItemClickListener
                navigationController.navigateToExternalBrowser(url)
            })
        }
        val layoutManager = GridLayoutManager(context, MAX_SPAN_SIZE).apply {
            spanSizeLookup = groupAdapter.spanSizeLookup
        }
        binding.sponsorRecycler.layoutManager = layoutManager
        binding.sponsorRecycler.adapter = groupAdapter
    }

    fun bindSponsorsToRecycler(sponsorPlans: List<SponsorPlan>) {
        val items = sponsorPlans.map { plan ->
            Section(SponsorPlanItem(plan)).apply {
                addAll(plan.groups.flatMap {
                    it.sponsors.map {
                        SponsorItem(this@SponsorsFragment, plan.type, it)
                    }
                })

                // fill dead spaces by dummy sponsors
                val columnSize = MAX_SPAN_SIZE / getSponsorItemSpanSize(plan.type, MAX_SPAN_SIZE)
                val modSize = (groupCount - 1) % columnSize

                if (modSize > 0) {
                    0.until(columnSize - modSize).forEach {
                        add(EmptySponsorItem(plan.type))
                    }
                }
            }
        }

        // A workaround for Groupie 2.0.0. ref: https://github.com/lisawray/groupie/issues/149
        // Update threw an IndexOutOfBoundsException when a nested section has been passed to update.
        if (sponsorPlansSection.itemCount == 0) {
            sponsorPlansSection.addAll(items)
        }
    }

    companion object {
        fun newInstance(): SponsorsFragment = SponsorsFragment()
    }
}
