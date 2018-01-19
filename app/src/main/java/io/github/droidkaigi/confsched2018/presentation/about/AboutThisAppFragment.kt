package io.github.droidkaigi.confsched2018.presentation.about

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.SimpleItemAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import io.github.droidkaigi.confsched2018.databinding.FragmentAboutThisAppBinding
import io.github.droidkaigi.confsched2018.di.Injectable
import io.github.droidkaigi.confsched2018.presentation.NavigationController
import io.github.droidkaigi.confsched2018.presentation.Result
import io.github.droidkaigi.confsched2018.presentation.common.binding.FragmentDataBindingComponent
import io.github.droidkaigi.confsched2018.presentation.contributor.item.AboutThisAppItem
import io.github.droidkaigi.confsched2018.presentation.contributor.item.AboutThisAppsSection
import timber.log.Timber
import javax.inject.Inject

class AboutThisAppFragment : Fragment(), Injectable {
    private lateinit var binding: FragmentAboutThisAppBinding
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject lateinit var navigationController: NavigationController
    private val fragmentDataBindingComponent = FragmentDataBindingComponent(this)
    private val aboutThisAppSection = AboutThisAppsSection(fragmentDataBindingComponent)

    private val viewModel: AboutThisAppViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(AboutThisAppViewModel::class.java)
    }

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
        viewModel.aboutThisApps.observe(this, Observer { result ->
            when (result) {
                is Result.Success -> {
                    aboutThisAppSection.updateAboutThisApps(
                            result.data,
                            onAboutThisHeaderIconClickListener
                    )
                }
                is Result.Failure -> {
                    Timber.e(result.e)
                }
            }
        })
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
    }

    companion object {
        fun newInstance(): AboutThisAppFragment = AboutThisAppFragment()
    }
}
