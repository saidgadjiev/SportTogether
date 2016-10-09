package ru.mail.sporttogether.mvp.presenters.map

import android.util.Log
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import ru.mail.sporttogether.app.App
import ru.mail.sporttogether.mvp.views.map.IMapView
import ru.mail.sporttogether.net.api.RestAPI
import ru.mail.sporttogether.net.models.Event
import ru.mail.sporttogether.net.responses.Response
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

/**
 * Created by bagrusss on 01.10.16.
 *
 */
class MapPresenterImpl : IMapPresenter, GoogleMap.OnMapClickListener {

    private var map: GoogleMap? = null
    private var view: IMapView? = null

    private var lastMarker: Marker? = null
    private var lastPos: LatLng? = null
    private val options: MarkerOptions = MarkerOptions().draggable(true)

    @Inject lateinit var api: RestAPI

    constructor(view: IMapView) {
        this.view = view
        App.injector.usePresenterComponent().inject(this)
    }

    override fun onPause() {
        map?.setOnMapClickListener(null)
    }

    override fun onResume() {
        map?.setOnMapClickListener(this)

    }

    override fun onDestroy() {
        view = null
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
        api.getAllEvents()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : Subscriber<Response<ArrayList<Event>>>() {
                    override fun onNext(t: Response<ArrayList<Event>>) {
                        Log.i("ok ", t.data.toString())
                    }

                    override fun onError(e: Throwable) {
                        Log.e("error = ", e.message, e)
                    }

                    override fun onCompleted() {
                    }

                })
    }

    override fun fabClicked() {
        lastPos?.let {
            view!!.startAddEventActivity(it.longitude, it.latitude)
        }
    }

    private fun addMarker(latlng: LatLng) {
        lastMarker?.let(Marker::remove)
        map?.let {
            this@MapPresenterImpl.lastPos = latlng
            view!!.showFab()
            options.position(latlng)
            lastMarker = it.addMarker(options)
        }
    }

}