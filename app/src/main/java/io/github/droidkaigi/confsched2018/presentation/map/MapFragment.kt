package io.github.droidkaigi.confsched2018.presentation.map

import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import io.github.droidkaigi.confsched2018.databinding.FragmentMapBinding
import io.github.droidkaigi.confsched2018.di.Injectable
import javax.inject.Inject

class MapFragment : Fragment(), Injectable, OnMapReadyCallback {

    private lateinit var binding: FragmentMapBinding
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var mapView: MapView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentMapBinding.inflate(inflater, container!!, false)

        with(binding.mapView) {
            mapView = this
            onCreate(savedInstanceState)
            getMapAsync(this@MapFragment)
        }

        return binding.root
    }

    override fun onMapReady(map: GoogleMap?) {
        // TODO
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    companion object {
        fun newInstance(): MapFragment = MapFragment()
    }
}
