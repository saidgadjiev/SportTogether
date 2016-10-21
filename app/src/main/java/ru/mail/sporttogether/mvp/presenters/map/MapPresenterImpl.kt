package ru.mail.sporttogether.mvp.presenters.map

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.iid.FirebaseInstanceId
import ru.mail.sporttogether.R
import ru.mail.sporttogether.app.App
import ru.mail.sporttogether.managers.events.IEventsManager
import ru.mail.sporttogether.mvp.views.map.IMapView
import ru.mail.sporttogether.net.api.EventsAPI
import ru.mail.sporttogether.net.models.Event
import ru.mail.sporttogether.net.models.EventsResponse
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
class MapPresenterImpl : IMapPresenter {

    private var map: GoogleMap? = null
    private var view: IMapView? = null

    private var lastMarker: Marker? = null
    private var lastPos: LatLng? = null
    private val options: MarkerOptions = MarkerOptions()

    private val markerIdEventMap = HashMap<String, Event>()

    @Inject lateinit var api: EventsAPI
    @Inject lateinit var eventsManager: IEventsManager

    constructor(view: IMapView) {
        this.view = view
        App.injector
                .usePresenterComponent()
                .inject(this)
    }

    override fun onPause() {
        map?.let {
            it.setOnMapClickListener(null)
            it.setOnMarkerClickListener(null)
        }
    }

    override fun onResume() {
        map?.let {
            it.setOnMapClickListener(this)
            it.setOnMarkerClickListener(this)
        }
    }

    override fun onDestroy() {
        view = null
    }

    override fun onMapClick(latlng: LatLng) {
        view?.hideInfo()
        addMarker(latlng)
    }

    override fun onMapReady(map: GoogleMap) {
        this.map = map
        map.isBuildingsEnabled = true
        map.isMyLocationEnabled = true
        map.uiSettings.isZoomControlsEnabled = true
        map.setOnMapClickListener(this)
        map.setOnMarkerClickListener(this)
        api.getAllEvents()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : Subscriber<Response<EventsResponse>>() {
                    override fun onNext(response: Response<EventsResponse>) {
                        if (response.code == 0) {
                            markerIdEventMap.clear()
                            eventsManager.swapEvents(response.data)
                            addMarkers(response.data)
                        }
                    }

                    override fun onError(e: Throwable) {
                        view?.showToast(R.string.cant_get_events)
                    }

                    override fun onCompleted() {

                    }

                })
    }

    private fun addMarkers(data: List<Event>) {
        for (event in data) {
            val latlng = LatLng(event.latitude, event.longtitude)
            val markerOptions = options.position(latlng).draggable(false)
            map?.let {
                val marker = it.addMarker(markerOptions)
                markerIdEventMap.put(marker.id, event)
            }
        }

    }

    override fun onAddButtonClicked() {
        lastPos?.let {
            view?.startAddEventActivity(it.longitude, it.latitude)
        }
    }

    private fun addMarker(latlng: LatLng) {
        lastMarker?.let(Marker::remove)
        map?.let {
            this@MapPresenterImpl.lastPos = latlng
            options.position(latlng).draggable(true)
            lastMarker = it.addMarker(options)
        }
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        showEventInfo(marker)
        return true
    }

    var lastEventId = 0L

    private fun showEventInfo(marker: Marker) {
        val event = markerIdEventMap[marker.id]
        event?.let {
            lastEventId = it.id!!
            view?.showInfo(it)
        }
    }

    override fun onBackPressed() {
        view?.finishView()
    }

    override fun onAngryButtonClicked() {

    }

    override fun onJoinButtonClicked() {
        api.joinToEvent(lastEventId, FirebaseInstanceId.getInstance().token)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : Subscriber<Response<Any>>() {
                    override fun onNext(t: Response<Any>) {
                        view?.showToast(android.R.string.ok)
                    }

                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {

                    }

                })
    }

}