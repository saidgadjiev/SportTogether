package ru.mail.sporttogether.mvp.presenters.map

import android.Manifest
import android.graphics.Point
import android.location.Location
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.iid.FirebaseInstanceId
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import ru.mail.sporttogether.R
import ru.mail.sporttogether.app.App
import ru.mail.sporttogether.eventbus.PermissionGrantedMessage
import ru.mail.sporttogether.eventbus.PermissionMessage
import ru.mail.sporttogether.managers.LocationManager
import ru.mail.sporttogether.managers.events.EventsManager
import ru.mail.sporttogether.mvp.views.map.IMapView
import ru.mail.sporttogether.net.api.EventsAPI
import ru.mail.sporttogether.net.models.Event
import ru.mail.sporttogether.net.models.EventsResponse
import ru.mail.sporttogether.net.responses.Response
import rx.Observable
import rx.Subscriber
import rx.Subscription
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
    private lateinit var lastPos: LatLng
    private val options: MarkerOptions = MarkerOptions()
    private var lastEventId = 0L

    private val markerIdEventMap = HashMap<String, Event>()

    @Inject lateinit var api: EventsAPI
    @Inject lateinit var eventsManager: EventsManager
    @Inject lateinit var locationManager: LocationManager

    private var apiSubscribtion: Subscription? = null
    private lateinit var locationSubscription: Subscription

    constructor(view: IMapView) {
        this.view = view
        App.injector
                .usePresenterComponent()
                .inject(this)
    }

    private var eventsSubscribion: Subscription? = null

    override fun onStart() {
        EventBus.getDefault().register(this)
        eventsSubscribion = eventsManager.getObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { newState ->
                    if (newState.type == EventsManager.UpdateType.ADD) {
                        val event = newState.data as Event
                        addMarker(LatLng(event.latitude, event.longtitude))
                    }
                }
    }

    override fun onStop() {
        EventBus.getDefault().unregister(this)
        eventsSubscribion?.unsubscribe()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(msg: PermissionGrantedMessage) {
        if (msg.reqCode === REQUEST_CODE) {
            locationManager.update(false)
        }
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
        locationSubscription.unsubscribe()
        view = null
        map?.let {
            with(it) {
                setOnCameraIdleListener(null)
                setOnMapClickListener(null)
                setOnMarkerClickListener(null)
                setOnCameraMoveStartedListener(null)
            }
        }
        map = null
    }

    override fun onMapClick(latlng: LatLng) {
        view?.hideInfo()
    }

    override fun onCameraMoveStarted(p0: Int) {
        map?.clear()
    }

    override fun onCameraIdle(x: Int, y: Int) {
        lastPos = map?.projection!!.fromScreenLocation(Point(x, y))
        apiSubscribtion?.unsubscribe()
        apiSubscribtion = calculateScale(lastPos, x)
                .subscribeOn(Schedulers.computation())
                .switchMap { distance ->
                    api.getEventsByDistanceAndPosition(Math.abs(distance), lastPos.latitude, lastPos.longitude)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<Response<EventsResponse>>() {
                    override fun onError(e: Throwable?) {
                        view?.showToast(R.string.cant_get_events)
                    }

                    override fun onNext(response: Response<EventsResponse>) {
                        markerIdEventMap.clear()
                        eventsManager.swapEvents(response.data)
                        addMarkers(response.data)
                    }

                    override fun onCompleted() {

                    }

                })
    }

    //будем брать по ширине экрана дистанцию
    fun calculateScale(latlng: LatLng, distance: Int): Observable<Double> {
        val res = map?.let {
            val kmPerPixel = 156.54303392 * Math.cos(latlng.latitude * Math.PI / 180) / Math.pow(2.0, it.cameraPosition.zoom.toDouble())
            kmPerPixel * distance
        } ?: 20.0
        return Observable.just(res)
    }

    override fun onMapReady(map: GoogleMap) {
        this.map = map
        map.isBuildingsEnabled = true
        map.uiSettings.isZoomControlsEnabled = true
        map.setOnMapClickListener(this)
        map.setOnMarkerClickListener(this)
        map.setOnCameraIdleListener(view)
        map.setOnCameraMoveStartedListener(this)
        locationSubscription = locationManager.locationUpdate.subscribe { location ->
            onLocationUpdated(location)
        }
        if (!locationManager.checkForPermissions()) {
            val permissions = Arrays.asList(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
            EventBus.getDefault().post(PermissionMessage(reqCode = REQUEST_CODE, permissionsList = permissions))
        } else {
            locationManager.getLocation()?.let {
                showMe(it)
            }
            locationManager.update(false)
        }
        /*api.getAllEvents()
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
                })*/
    }

    private fun onLocationUpdated(location: Location) {
        showMe(location)
    }

    private fun showMe(location: Location) {
        map?.let {
            val latlng = LatLng(location.latitude, location.longitude)
            it.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15f))
        }

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
        map?.let {
            lastPos = latlng
            options.position(latlng).draggable(true)
            lastMarker = it.addMarker(options)
        }
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        showEventInfo(marker)
        return true
    }

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
        api.report(lastEventId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : Subscriber<Response<Any>>() {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {
                        view?.showToast("Произошла ошибка")
                    }

                    override fun onNext(t: Response<Any>) {
                        view?.showToast(android.R.string.ok)
                    }
                })
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

    override fun onPermissionsGranted(requestCode: Int) {
        if (requestCode === REQUEST_CODE) {
            locationManager.update(false)
        }
    }

    companion object {
        @JvmStatic private val REQUEST_CODE = 1002
    }

}