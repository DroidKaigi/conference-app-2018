package io.github.droidkaigi.confsched2018.presentation.map

import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.droidkaigi.confsched2018.databinding.FragmentMapBinding
import io.github.droidkaigi.confsched2018.di.Injectable
import javax.inject.Inject

class MapFragment : Fragment(), Injectable {
    private lateinit var binding: FragmentMapBinding
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentMapBinding.inflate(inflater, container!!, false)
        return binding.root
    }

    companion object {
        fun newInstance(): MapFragment = MapFragment()
    }
}
