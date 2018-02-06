package io.github.droidkaigi.confsched2018.presentation.about

import android.os.Bundle
import android.support.v7.widget.SimpleItemAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import dagger.android.support.DaggerFragment
import io.github.droidkaigi.confsched2018.data.db.fixeddata.AboutThisApps
import io.github.droidkaigi.confsched2018.databinding.FragmentAboutThisAppBinding
import io.github.droidkaigi.confsched2018.presentation.NavigationController
import io.github.droidkaigi.confsched2018.presentation.about.item.AboutThisAppItem
import io.github.droidkaigi.confsched2018.presentation.about.item.AboutThisAppsSection
import javax.inject.Inject

class AboutThisAppFragment : DaggerFragment() {
    private lateinit var binding: FragmentAboutThisAppBinding
    @Inject lateinit var navigationController: NavigationController
    private val aboutThisAppSection = AboutThisAppsSection()

    private val onAboutThisHeaderIconClickListener = { navUrl: String ->
        navigationController.navigateToExternalBrowser(navUrl)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentAboutThisAppBinding.inflate(inflater, container!!, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val groupAdapter = GroupAdapter<ViewHolder>().apply {
            add(aboutThisAppSection)
            setOnItemClickListener({ item, _ ->
                val aboutThisAppItem = item as? AboutThisAppItem ?: return@setOnItemClickListener
                navigationController.navigateImplicitly(aboutThisAppItem.aboutThisApp.navigationUrl)
            })
        }
        binding.aboutThisAppRecycler.apply {
            adapter = groupAdapter
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        }
        aboutThisAppSection.updateAboutThisApps(
                AboutThisApps.getThisApps(),
                onAboutThisHeaderIconClickListener
        )
    }

    companion object {
        fun newInstance(): AboutThisAppFragment = AboutThisAppFragment()
    }
}
