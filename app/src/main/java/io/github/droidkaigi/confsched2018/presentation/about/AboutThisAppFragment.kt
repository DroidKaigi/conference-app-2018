package io.github.droidkaigi.confsched2018.presentation.about

import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.droidkaigi.confsched2018.databinding.FragmentAboutThisAppBinding
import io.github.droidkaigi.confsched2018.di.Injectable
import io.github.droidkaigi.confsched2018.presentation.NavigationController
import javax.inject.Inject

class AboutThisAppFragment : Fragment(), Injectable {
    private lateinit var binding: FragmentAboutThisAppBinding
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject lateinit var navigationController: NavigationController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentAboutThisAppBinding.inflate(inflater, container!!, false)
        return binding.root
    }

    //TODO add transition to license screen. (just refer like navigationController.navigateToOssLicensesMenuActivity())

    companion object {
        fun newInstance(): AboutThisAppFragment = AboutThisAppFragment()
    }
}
