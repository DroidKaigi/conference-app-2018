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
import com.xwray.groupie.Item
import com.xwray.groupie.Section
import com.xwray.groupie.ViewHolder
import io.github.droidkaigi.confsched2018.databinding.FragmentSponsorsBinding
import io.github.droidkaigi.confsched2018.di.Injectable
import io.github.droidkaigi.confsched2018.model.SponsorPlan
import io.github.droidkaigi.confsched2018.presentation.Result
import io.github.droidkaigi.confsched2018.presentation.sponsors.item.SponsorItem
import io.github.droidkaigi.confsched2018.presentation.sponsors.item.SponsorPlanItem
import io.github.droidkaigi.confsched2018.util.ext.observe
import timber.log.Timber
import javax.inject.Inject

class SponsorsFragment : Fragment(), Injectable {
    private lateinit var binding: FragmentSponsorsBinding
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

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
        binding.sponsorRecycler.layoutManager = GridLayoutManager(context, 2)
        val groupAdapter = GroupAdapter<ViewHolder>().apply {
            add(sponsorPlansSection)
            setOnItemClickListener({ _, _ ->
                //TODO
            })
        }
        binding.sponsorRecycler.adapter = groupAdapter
    }

    fun bindSponsorsToRecycler(sponsorPlans: List<SponsorPlan>) {
        sponsorPlansSection.update(
                sponsorPlans.flatMap {
                    arrayListOf<Item<*>>(SponsorPlanItem(it)).apply {
                        addAll(
                                it.groups.flatMap {
                                    it.sponsors.map {
                                        SponsorItem(it)
                                    }
                                }
                        )
                    }
                }
        )
    }

    companion object {
        fun newInstance(): SponsorsFragment = SponsorsFragment()
    }
}
