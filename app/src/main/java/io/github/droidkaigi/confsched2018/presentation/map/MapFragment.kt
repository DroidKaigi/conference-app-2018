package io.github.droidkaigi.confsched2018.presentation.map

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.net.toUri
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.android.support.DaggerFragment
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.databinding.FragmentMapBinding

class MapFragment : DaggerFragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentMapBinding
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.placeText.setOnClickListener {
            val placeName = context?.getString(R.string.map_place_name)
            val placeUri = "geo:0,0?q=$PLACE_LAT,$PLACE_LNG($placeName)".toUri()
            val mapIntent = Intent().apply {
                action = Intent.ACTION_VIEW
                data = placeUri
            }
            startActivity(mapIntent)
        }
    }

    override fun onMapReady(map: GoogleMap?) {
        map?.run {
            val latLng = LatLng(PLACE_LAT, PLACE_LNG)

            // custom pin
            val pin: BitmapDescriptor = BitmapDescriptorFactory.fromBitmap(getBitmap(R.drawable
                    .ic_place_orange_36dp))

            val marker = addMarker(MarkerOptions()
                    .position(latLng)
                    .icon(pin)
                    .title(context?.getString(R.string.map_place_name)))
            marker.showInfoWindow()

            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 16f)
            moveCamera(cameraUpdate)
        }
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

    private fun getBitmap(@DrawableRes resource: Int): Bitmap {
        val drawable = ContextCompat.getDrawable(context!!, resource)
        val canvas = Canvas()
        val bitmap = Bitmap.createBitmap(drawable!!.intrinsicWidth, drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888)
        canvas.setBitmap(bitmap)
        drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        drawable.draw(canvas)
        return bitmap
    }

    companion object {
        private const val PLACE_LAT = 35.6957954
        private const val PLACE_LNG = 139.69038920000003

        fun newInstance(): MapFragment = MapFragment()
    }
}
