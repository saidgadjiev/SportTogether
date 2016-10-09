package ru.mail.sporttogether.mvp.presenters.map

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import ru.mail.sporttogether.R
import ru.mail.sporttogether.app.App
import ru.mail.sporttogether.mvp.views.map.IMapView
import ru.mail.sporttogether.net.api.RestAPI
import ru.mail.sporttogether.net.models.Event
import ru.mail.sporttogether.net.models.EventsResponse
import ru.mail.sporttogether.net.responses.Response
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by bagrusss on 01.10.16.
 *
 */
class MapPresenterImpl : IMapPresenter, GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener {

    private var map: GoogleMap? = null
    private var view: IMapView? = null

    private var lastMarker: Marker? = null
    private var lastPos: LatLng? = null
    private val options: MarkerOptions = MarkerOptions()

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
                .subscribe(object : Subscriber<Response<EventsResponse>>() {
                    override fun onNext(response: Response<EventsResponse>) {
                        addMarkers(response.data)
                    }

                    override fun onError(e: Throwable) {
                        view?.showToast(R.string.cant_get_events)
                    }

                    override fun onCompleted() {
                    }

                })
    }

    private fun addMarkers(data: List<Event>) {
        map?.let {
            data.forEach {
                val latlng = LatLng(it.latitude, it.longtitude)
                val marker = options.position(latlng).draggable(false)
                map!!.addMarker(marker)
            }
        }

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
            options.position(latlng).draggable(true)
            lastMarker = it.addMarker(options)
        }
    }

    override fun onMarkerClick(marker: Marker): Boolean {

        return true
    }

}