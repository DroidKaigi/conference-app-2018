package io.github.droidkaigi.confsched2018.presentation.sponsors

import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.droidkaigi.confsched2018.databinding.FragmentSponsorsBinding
import io.github.droidkaigi.confsched2018.di.Injectable
import javax.inject.Inject

class SponsorsFragment : Fragment(), Injectable {
    private lateinit var binding: FragmentSponsorsBinding
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentSponsorsBinding.inflate(inflater, container!!, false)
        return binding.root
    }

    companion object {
        fun newInstance(): SponsorsFragment = SponsorsFragment()
    }
}
