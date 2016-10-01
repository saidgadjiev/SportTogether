package ru.mail.sporttogether.mvp.presenters.map

import android.view.View
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import ru.mail.sporttogether.activities.AddEventActivity
import ru.mail.sporttogether.mvp.views.map.IMapView

/**
 * Created by bagrusss on 01.10.16.
 *
 */
class MapPresenterImpl : IMapPresenter, GoogleMap.OnMapClickListener {

    private var map: GoogleMap? = null
    private lateinit var view: IMapView

    private var lastMarker: Marker? = null
    private var lastPos: LatLng? = null
    private val options: MarkerOptions = MarkerOptions().draggable(true)

    constructor(view: IMapView) {
        this.view = view
    }

    override fun onPause() {
        map?.setOnMapClickListener(null)
    }

    override fun onResume() {
        map?.let {
            it.setOnMapClickListener(this)
        }
    }

    override fun onFabClicked(v: View) {
        lastPos?.let {
            //TODO нужен app context пока что так далее dependency injection
            AddEventActivity.start(v.context, it.longitude, it.latitude)
        }
    }

    override fun onMapClick(latlng: LatLng) {
        addMarker(latlng)
    }

    override fun onMapReady(map: GoogleMap) {
        this.map = map
        map.isBuildingsEnabled = true
        map.isMyLocationEnabled = true
        map.uiSettings.isZoomControlsEnabled = true
        map.setOnMapClickListener(this)
    }

    private fun addMarker(latlng: LatLng) {
        lastMarker?.let(Marker::remove)
        map?.let {
            this@MapPresenterImpl.lastPos = latlng
            options.position(latlng)
            lastMarker = it.addMarker(options)
        }
    }

}