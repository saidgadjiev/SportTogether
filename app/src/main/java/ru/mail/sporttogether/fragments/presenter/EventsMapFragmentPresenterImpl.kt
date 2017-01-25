package ru.mail.sporttogether.fragments.presenter

import android.Manifest
import android.content.Context
import android.graphics.Point
import android.location.Location
import android.os.Bundle
import android.util.Log
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.iid.FirebaseInstanceId
import ru.mail.sporttogether.R
import ru.mail.sporttogether.app.App
import ru.mail.sporttogether.auth.core.SocialNetworkManager
import ru.mail.sporttogether.fragments.view.EventsMapView
import ru.mail.sporttogether.managers.EventsManager
import ru.mail.sporttogether.managers.LocationManager
import ru.mail.sporttogether.net.EventsResponse
import ru.mail.sporttogether.net.Response
import ru.mail.sporttogether.net.api.EventsAPI
import ru.mail.sporttogether.net.api.ServiceApi
import ru.mail.sporttogether.net.api.YandexMapsApi
import ru.mail.sporttogether.net.models.Event
import ru.mail.sporttogether.net.models.IpResponse
import ru.mail.sporttogether.net.models.Task
import ru.mail.sporttogether.net.models.User
import ru.mail.sporttogether.net.models.yandex.maps.GeoObject
import ru.mail.sporttogether.utils.MapUtils
import rx.Observable
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.*
import javax.inject.Inject


/**
 * Created by bagrusss on 01.10.16
 */
class EventsMapFragmentPresenterImpl(var view: EventsMapView?) : EventsMapFragmentPresenter {

    private var map: GoogleMap? = null

    private var lastMarker: Marker? = null
    private lateinit var lastPos: LatLng
    private lateinit var userLocation: LatLng

    private val options = MarkerOptions()

    private lateinit var lastEvent: Event
    private var lastEventTasks: ArrayList<Task>? = null

    private val markerIdEventMap = HashMap<String, Event>()

    @Inject lateinit var api: EventsAPI
    @Inject lateinit var eventsManager: EventsManager
    @Inject lateinit var locationManager: LocationManager
    @Inject lateinit var socialNetworkManager: SocialNetworkManager
    @Inject lateinit var yandexApi: YandexMapsApi
    @Inject lateinit var serviceApi: ServiceApi
    @Inject lateinit var context: Context

    private var userId: Long = 0

    private var apiSubscribtion: Subscription? = null
    private var locationSubscription: Subscription? = null

    private var eventsSubscribion: Subscription? = null
    private var userPositionFound = false

    init {
        App.injector
                .usePresenterComponent()
                .inject(this)
        userId = socialNetworkManager.activeUser!!.id
    }


    override fun onCreate(args: Bundle?) {
        eventsSubscribion = eventsManager.getObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { newState ->
                    when (newState.type) {
                        EventsManager.UpdateType.ADD -> {
                            val event = newState.data as Event
                            addMarker(LatLng(event.lat, event.lng))
                        }
                        EventsManager.UpdateType.NEED_SHOW -> {
                            val event = newState.data as Event
                            map?.animateCamera(CameraUpdateFactory.newLatLng(LatLng(event.lat, event.lng)))
                            view?.showMap()
                            lastEvent = event
                            val isCancelable = (userId == event.user.id) and !event.isEnded
                            view?.showInfo(lastEvent, isCancelable, null)
                            loadAddressFromYandex(event.lat, event.lng)
                        }

                        EventsManager.UpdateType.NEED_SHOW_POSITION -> {
                            val event = newState.data as Event
                            map?.animateCamera(CameraUpdateFactory.newLatLng(LatLng(event.lat, event.lng)))
                            view?.showMap()
//                            lastEvent = event
//                            val isCancelable = (userId == event.user.id) and !event.isEnded
//                            view?.showInfo(lastEvent, isCancelable, null)
//                            loadAddressFromYandex(event.lat, event.lng)
                        }
                    }
                }
    }

    override fun onStop() {
        if (userPositionFound)
            locationSubscription?.unsubscribe()
    }

    override fun onPause() {
        map?.let {
            it.setOnMapClickListener(null)
            it.setOnMarkerClickListener(null)
            it.setOnCameraIdleListener(null)
        }
    }

    override fun onResume() {
        map?.let {
            it.setOnMapClickListener(this)
            it.setOnCameraIdleListener(view)
            it.setOnMarkerClickListener(this)
        }
    }

    override fun onDestroy() {
        locationSubscription?.unsubscribe()
        eventsSubscribion?.unsubscribe()
        view = null
        map?.let {
            with(it) {
                setOnCameraIdleListener(null)
            }
        }
        map = null
        locationManager.endLocationUpdate()
    }

    override fun checkLocation() {
        if (!locationManager.checkLocationEnabled(context)) {
            view?.onLocationNotChecked()
        }
    }


    private fun initMap() {
        if (!locationManager.checkLocationEnabled(context)) {
            serviceApi.getLocationByIP()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Subscriber<IpResponse>() {
                        override fun onError(e: Throwable) {
                            unsubscribe()
                            view?.onLocationNotChecked()
                        }

                        override fun onCompleted() {

                        }

                        override fun onNext(t: IpResponse) {
                            unsubscribe()
                            userLocation = LatLng(t.lat, t.lon)
                            map?.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15f))
                            view?.onLocationNotChecked()
                        }

                    })
        } else {
            locationManager.getLocation()?.let {
                userLocation = LatLng(it.latitude, it.longitude)
                showMe(it)
            }
            locationManager.update(false)
            map?.isMyLocationEnabled = true
        }
    }


    override fun onLocationEnabled() {
        initMap()
    }

    override fun onMapReady(map: GoogleMap) {
        this.map = map
        map.isBuildingsEnabled = true
        map.setOnMapClickListener(this)
        map.setOnMarkerClickListener(this)
        map.setOnCameraIdleListener(view)

        locationSubscription = locationManager.locationUpdate.subscribe { location ->
            userLocation = LatLng(location.latitude, location.longitude)
            onLocationUpdated(location)
        }
        if (!locationManager.checkForPermissions()) {
            val permissions = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
            view?.checkLocationPermissions(permissions)
        } else {
            initMap()
        }
    }

    override fun onMapClick(latlng: LatLng) {
        view?.hideInfo()
    }

    override fun zoomInClicked() {
        map?.animateCamera(CameraUpdateFactory.zoomIn())
    }

    override fun zoomOutClicked() {
        map?.animateCamera(CameraUpdateFactory.zoomOut())
    }

    override fun loadEvents() {

    }

    private fun loadAddressFromYandex(lat: Double, lng: Double) {
        yandexApi.getAddressByCoordinates(longlat = "" + lng + "," + lat)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : Subscriber<ArrayList<GeoObject>>() {
                    override fun onNext(t: ArrayList<GeoObject>) {
                        if (t.isNotEmpty()) {
                            view?.updateAddress(t[0].textAddress)
                        }
                        unsubscribe()
                    }

                    override fun onError(e: Throwable) {
                        Log.e("yandex", e.message, e)
                        unsubscribe()
                    }

                    override fun onCompleted() {

                    }

                })
    }

    override fun fabClicked(isBottomSheet: Boolean) {
        if (!isBottomSheet) {
            lastPos.let {
                view?.startAddEventActivity(it.longitude, it.latitude)
            }
        } else {
            view?.shareResults(lastEvent)
        }
    }

    override fun searchByCategory(s: String) {
        apiSubscribtion?.unsubscribe()
        apiSubscribtion = api.getEventsByCategory(s)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .flatMap { resp ->
                    val code = resp.code
                    if (code == 0) {
                        val list = resp.data
                        for (i in 0..list.size - 1)
                            list[i].distance = MapUtils.distanceBetweenPoints(userLocation, LatLng(list[i].lat, list[i].lng))

                        Collections.sort(list, { o1, o2 ->
                            return@sort if (o1.distance > o2.distance) 1 else if (o1.distance == o2.distance) 0 else -1
                        })
                        return@flatMap Observable.just(list)
                    }
                    throw RuntimeException("error code $code")
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<ArrayList<Event>>() {
                    override fun onNext(t: ArrayList<Event>) {
                        view?.loadEvents(t)
                        eventsManager.swapEvents(t)
                        addMarkers(t)
                    }

                    override fun onError(e: Throwable?) {

                    }

                    override fun onCompleted() {
                    }

                })
    }

    override fun loadTasks(event: Event) {
        lastEvent = event
        api.getTasksByEventId(event.id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : Subscriber<Response<ArrayList<Task>>>() {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {
                        view?.showToast("Произошла ошибка")
                        Log.e(TAG, e.message, e)
                    }

                    override fun onNext(resp: Response<ArrayList<Task>>) {
                        if (resp.code == 0) {
                            lastEventTasks = resp.data
                            view?.onFinishLoadTasks(lastEventTasks)
                            render()
                        } else view?.showToast(resp.message)
                    }
                })
    }

    override fun cancelEvent() {
        apiSubscribtion?.unsubscribe()
        api.cancelEvent(lastEvent.id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : Subscriber<Response<Any>>() {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {

                    }

                    override fun onNext(t: Response<Any>) {
                        if (t.code == 0) {
                            view?.let {
                                it.hideInfo()
                                it.showToast("Событие отменено")
                                //это костыль, чтобы удалялся маркер при отмене события
                                it.onCameraIdle()
                            }
                            lastMarker?.let {
                                markerIdEventMap.remove(it.id)
                                it.remove()
                            }
                        } else {
                            view?.showToast(t.message)
                        }
                    }

                })
    }

    override fun onCameraIdle(x: Int, y: Int) {
        map?.let { map ->
            val cameraPosition = map.cameraPosition
            if (cameraPosition.zoom > MAX_ZOOM) {
                map.animateCamera(CameraUpdateFactory.zoomTo(MAX_ZOOM))
                return
            }
            if (cameraPosition.zoom < MIN_ZOOM) {
                map.animateCamera(CameraUpdateFactory.zoomTo(MIN_ZOOM))
                return
            }
            lastPos = map.projection.fromScreenLocation(Point(x, y))
            apiSubscribtion?.unsubscribe()
            apiSubscribtion = calculateScale(lastPos, x)
                    .subscribeOn(Schedulers.computation())
                    .switchMap { distance ->
                        api.getEventsByDistanceAndPosition(Math.abs(distance), lastPos.latitude, lastPos.longitude)
                    }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Subscriber<Response<EventsResponse>>() {
                        override fun onError(e: Throwable) {
                            Log.e("EventsMapPresenter", "events loaded error")
                            view?.showToast(R.string.cant_get_events)
                        }

                        override fun onNext(response: Response<EventsResponse>) {
                            Log.d("EventsMapPresenter", "events loaded")
                            eventsManager.swapEvents(response.data)
                            addMarkers(response.data)
                        }

                        override fun onCompleted() {

                        }

                    })
        }
    }

    override fun getMyId(): Long {
        return socialNetworkManager.activeUser!!.id
    }

    fun calculateScale(latlng: LatLng, distance: Int): Observable<Double> {
        val res = map?.let {
            val kmPerPixel = 156.54303392 * Math.cos(latlng.latitude * Math.PI / 180) / Math.pow(2.0, it.cameraPosition.zoom.toDouble())
            kmPerPixel * distance
        } ?: 20.0
        return Observable.just(res)
    }

    private fun onLocationUpdated(location: Location) {
        showMe(location)
    }

    private fun showMe(location: Location) {
        map?.let {
            userPositionFound = true
            lastPos = LatLng(location.latitude, location.longitude)
            it.moveCamera(CameraUpdateFactory.newLatLngZoom(lastPos, 15f))
            locationSubscription?.unsubscribe()
        }

    }

    private fun addMarkers(data: List<Event>) {
        map?.let {
            markerIdEventMap.clear()
            it.clear()
            for (event in data) {
                val latlng = LatLng(event.lat, event.lng)
                options.position(latlng)
                if (event.isEnded)
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                else options.icon(null)
                val marker = it.addMarker(options)
                markerIdEventMap.put(marker.id, event)
            }
            options.icon(null)
            Log.v("EventsMapPresenter", "add events")
        }
    }

    private fun addMarker(latlng: LatLng) {
        map?.let {
            lastPos = latlng
            options.position(latlng)
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
            lastMarker = marker
            lastEvent = it
            lastEventTasks = null //обнуляем таски, когда только кликнули по маркеру, считается, что когда массив = null, таски не загружены
            val isCancelable = (userId == event.user.id) and !event.isEnded
            loadAddressFromYandex(event.lat, event.lng)
            map?.animateCamera(CameraUpdateFactory.newLatLng(LatLng(event.lat, event.lng)))
            view?.showInfo(lastEvent, isCancelable, lastEventTasks)
        }
    }

    private fun render() {
        val isCancelable = (userId == lastEvent.user.id) and !lastEvent.isEnded
        view?.render(lastEvent, isCancelable, lastEventTasks)
    }

    override fun onBackPressed() {
        view?.finishView()
    }

    override fun doAngry() {
        api.report(lastEvent.id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : Subscriber<Response<Any>>() {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {
                        view?.showToast("Произошла ошибка")
                        Log.e(TAG, e.message, e)
                    }

                    override fun onNext(t: Response<Any>) {
                        if (t.code == 0) {
                            lastEvent.reports += 1
                            lastEvent.isReported = true
                            view?.showToast(android.R.string.ok)
                            render()
                        } else view?.showToast("Вы уже жаловались на это событие")
                    }
                })
    }

    override fun doJoin() {
        if (lastEvent.isEnded) {
            view?.showToast("Событие уже завершилось")
        } else
            if (lastEvent.isJoined) {
                unJoin()
            } else {
                join()
            }
    }

    fun join() {
        api.joinToEvent(lastEvent.id, FirebaseInstanceId.getInstance().token)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : Subscriber<Response<Any>>() {
                    override fun onNext(t: Response<Any>) {
                        if (t.code === 0) {
                            lastEvent.nowPeople += 1
                            lastEvent.isJoined = true
                            view?.showToast(android.R.string.ok)
                            render()
                        } else view?.showToast(t.message)
                    }

                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {

                    }

                })
    }

    private fun unJoin() {
        api.unjoinFromEvent(lastEvent.id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : Subscriber<Response<Any>>() {
                    override fun onNext(t: Response<Any>) {
                        if (t.code === 0) {
                            lastEvent.nowPeople -= 1
                            lastEvent.isJoined = false
                            //после отсоединения от события обнуляем все задачи, отмеченные нами
                            lastEventTasks?.forEach { task ->
                                if (task.user?.id == userId) {
                                    task.user = null
                                }
                            }
                            view?.showToast(android.R.string.ok)
                            render()
                        } else view?.showToast(t.message)
                    }

                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {

                    }

                })
    }

    override fun checkTask(task: Task) {
        api.checkTask(task).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : Subscriber<Response<Any>>() {
                    override fun onCompleted() {
                    }

                    override fun onError(e: Throwable) {
                        view?.showToast("Произошла ошибка")
                        Log.e(TAG, e.message, e)
                    }

                    override fun onNext(t: Response<Any>) {
                        if (t.code === 0) {
                            view?.showToast("Вы приняли на себя задачу : " + task.message)
                            val tasks = lastEventTasks
                            val changedTask = tasks?.find { it.id == task.id }
                            if (changedTask != null) {
                                val index = tasks?.indexOf(changedTask)!!.or(0)
                                tasks?.remove(changedTask)
                                val activeUser = socialNetworkManager.activeUser!!.copy()
                                val newUser = User("", activeUser.id, 0, activeUser.name, activeUser.avatar)
                                tasks?.add(index, changedTask.copy(user = newUser))
                            }
                            render()
                        } else view?.showToast(t.message)
                    }
                })
    }

    override fun uncheckTask(task: Task) {
        //потому что чтобы снять надо повторно отправить. Такой вот веселый сервер
        api.checkTask(task).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : Subscriber<Response<Any>>() {
                    override fun onCompleted() {
                    }

                    override fun onError(e: Throwable) {
                        view?.showToast("Произошла ошибка")
                        Log.e(TAG, e.message, e)
                    }

                    override fun onNext(t: Response<Any>) {
                        if (t.code === 0) {
                            view?.showToast("Вы отменили задачу : " + task.message)
                            val tasks = lastEventTasks
                            val changedTask = tasks?.find { it.id == task.id }
                            if (changedTask != null) {
                                val index = tasks?.indexOf(changedTask)!!.or(0)
                                tasks?.remove(changedTask)
                                tasks?.add(index, changedTask.copy(user = null))
                            }
                            render()
                        } else view?.showToast(t.message)
                    }
                })
    }

    override fun onPermissionsGranted(requestCode: Int) {
        initMap()
    }

    companion object {

        val TAG = "#MY " + EventsMapFragmentPresenterImpl::class.java.simpleName.substring(0, 18)

        @JvmStatic private val MAX_ZOOM = 17f
        @JvmStatic private val MIN_ZOOM = 10f

    }


}