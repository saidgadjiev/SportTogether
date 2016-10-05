package ru.mail.sporttogether.activities

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import com.google.android.gms.maps.MapView
import ru.mail.sporttogether.R
import ru.mail.sporttogether.data.binding.FabListener
import ru.mail.sporttogether.databinding.ActivityMapBinding
import ru.mail.sporttogether.mvp.presenters.map.IMapPresenter
import ru.mail.sporttogether.mvp.presenters.map.MapPresenterImpl
import ru.mail.sporttogether.mvp.views.map.IMapView

class MapActivity : AbstractActivity(), IMapView, FabListener {

    private lateinit var mapView: MapView
    private lateinit var binding: ActivityMapBinding

    private lateinit var presenter: IMapPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_map)
        mapView = binding.mapview
        mapView.onCreate(savedInstanceState)
        presenter = MapPresenterImpl(this)
        mapView.getMapAsync(presenter)
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
        binding.fabListener = this
        presenter.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
        binding.fabListener = null
        presenter.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
        presenter.onDestroy()
    }

    override fun onFabClicked(v: View) {
        v.isEnabled = false
        presenter.fabClicked()
        v.isEnabled = true
    }

    override fun startAddEventActivity(lng: Double, lat: Double) {
        AddEventActivity.start(this, lng, lat)
    }

}
