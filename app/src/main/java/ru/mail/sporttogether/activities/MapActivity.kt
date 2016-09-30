package ru.mail.sporttogether.activities

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import ru.mail.sporttogether.R
import ru.mail.sporttogether.data.binding.FabListener
import ru.mail.sporttogether.databinding.ActivityMapBinding

class MapActivity :
        BaseActivity(),
        OnMapReadyCallback,
        FabListener,
        GoogleMap.OnMapClickListener {

    private lateinit var mapView: MapView
    private lateinit var map: GoogleMap
    private lateinit var binding: ActivityMapBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_map)
        mapView = binding.mapview
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
        binding.fabListener = this
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
        binding.fabListener = null
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onPause() {
        super.onPause()
        mapView.onResume()
    }

    override fun onMapReady(map: GoogleMap) {
        this.map = map
        map.isBuildingsEnabled = true
        map.isMyLocationEnabled = true
        map.setOnMapClickListener(this)
    }

    override fun onFabClicked(v: View) {
        v.isEnabled = false
        v.isEnabled = true
    }

    override fun onMapClick(latlng: LatLng) {

    }

}
