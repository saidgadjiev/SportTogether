package ru.mail.sporttogether.mvp.presenters.map

import android.Manifest
import android.graphics.Point
import android.location.Location
import android.util.Log

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.iid.FirebaseInstanceId
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import ru.mail.sporttogether.R
import ru.mail.sporttogether.app.App
import ru.mail.sporttogether.auth.core.SocialNetworkManager
import ru.mail.sporttogether.eventbus.PermissionGrantedMessage
import ru.mail.sporttogether.eventbus.PermissionMessage
import ru.mail.sporttogether.managers.LocationManager
import ru.mail.sporttogether.managers.events.EventsManager
import ru.mail.sporttogether.mvp.views.map.IMapView
import ru.mail.sporttogether.net.api.EventsAPI
import ru.mail.sporttogether.net.api.ServiceApi
import ru.mail.sporttogether.net.api.YandexMapsApi
import ru.mail.sporttogether.net.models.Event
import ru.mail.sporttogether.net.models.IpResponse
import ru.mail.sporttogether.net.models.Task
import ru.mail.sporttogether.net.models.User
import ru.mail.sporttogether.net.models.yandex.maps.GeoObject
import ru.mail.sporttogether.net.responses.EventsResponse
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
class MapPresenterImpl(var view: IMapView?) : IMapPresenter {

    private var map: GoogleMap? = null

    private var lastMarker: Marker? = null
    private lateinit var lastPos: LatLng
    private val options: MarkerOptions = MarkerOptions()

    private lateinit var lastEvent: Event
    private var lastEventTasks: ArrayList<Task>? = null

    private val markerIdEventMap = HashMap<String, Event>()

    @Inject lateinit var api: EventsAPI
    @Inject lateinit var eventsManager: EventsManager
    @Inject lateinit var locationManager: LocationManager
    @Inject lateinit var yandexApi: YandexMapsApi
    @Inject lateinit var serviceApi: ServiceApi

    private val userId: Long

    private var apiSubscribtion: Subscription? = null
    private var locationSubscription: Subscription? = null

    private var eventsSubscribion: Subscription? = null
    private var eventSubscribtion: Subscription? = null

    init {
        App.injector
                .usePresenterComponent()
                .inject(this)
        userId = SocialNetworkManager.instance.activeUser.id //TODO inject network manager
    }

    override fun onStart() {
        EventBus.getDefault().register(this)
        eventsSubscribion = eventsManager.getObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { newState ->
                    if (newState.type == EventsManager.UpdateType.ADD) {
                        val event = newState.data as Event
                        addMarker(LatLng(event.lat, event.lng))
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
            it.setOnCameraMoveListener(null)
        }
    }

    override fun onResume() {
        map?.let {
            it.setOnMapClickListener(this)
            it.setOnMarkerClickListener(this)
            it.setOnCameraMoveStartedListener(this)
        }
    }

    override fun onDestroy() {
        locationSubscription?.unsubscribe()
        eventSubscribtion?.unsubscribe()
        view = null
        map?.let {
            with(it) {
                setOnCameraIdleListener(null)
                setOnMapClickListener(null)
                setOnCameraMoveStartedListener(null)
            }
        }
        map = null
    }

    override fun onMapClick(latlng: LatLng) {
        view?.hideInfo()
    }

    override fun onCameraMoveStarted(p0: Int) {

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
                    }

                    override fun onError(e: Throwable) {
                        Log.e("yandex", e.message, e)
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
            view?.shareResults()
        }
    }

    override fun searchByCategory(s: String) {
        apiSubscribtion?.unsubscribe()
        apiSubscribtion = api.getEventsByCategory(s)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : Subscriber<Response<EventsResponse>>() {
                    override fun onNext(t: Response<EventsResponse>) {
                        if (t.code == 0) {
                            view?.loadEvents(t.data)
                            markerIdEventMap.clear()
                            map?.clear()
                            eventsManager.swapEvents(t.data)
                            addMarkers(t.data)
                        }
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
                    }

                    override fun onNext(resp: Response<ArrayList<Task>>) {
                        if (resp.code === 0) {
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
                            view?.showToast("Событие отменено")
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
        map.setOnMapClickListener(this)
        map.setOnMarkerClickListener(this)
        map.setOnCameraIdleListener(view)

        serviceApi.getLocationByIP()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<IpResponse>() {
                    override fun onError(e: Throwable) {
                        view?.showToast(R.string.cant_load_position)
                    }

                    override fun onCompleted() {

                    }

                    override fun onNext(t: IpResponse) {
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(t.lat, t.lon), 15f))
                    }

                })

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
    }

    private fun onLocationUpdated(location: Location) {
        showMe(location)
    }

    private fun showMe(location: Location) {
        map?.let {
            lastPos = LatLng(location.latitude, location.longitude)
            it.moveCamera(CameraUpdateFactory.newLatLngZoom(lastPos, 15f))
            locationSubscription?.unsubscribe()
        }

    }

    private fun addMarkers(data: List<Event>) {
        map?.let {
            it.clear()
            for (event in data) {
                val latlng = LatLng(event.lat, event.lng)
                val markerOptions = options.position(latlng)
                if (event.isEnded)
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                else options.icon(null)
                val marker = it.addMarker(markerOptions)
                markerIdEventMap.put(marker.id, event)
            }
            options.icon(null)
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
            lastEvent = it
            lastEventTasks = null //обнуляем таски, когда только кликнули по маркеру, считается, что когда массив = null, таски не загружены
            val isCancelable = (userId == event.user.id) and !event.isEnded
            view?.showInfo(lastEvent, isCancelable, lastEventTasks)
            loadAddressFromYandex(event.lat, event.lng)
            map?.animateCamera(CameraUpdateFactory.newLatLng(LatLng(event.lat, event.lng)))
        }
    }

    private fun render() {
        val isCancelable = (userId === lastEvent.user.id) and !lastEvent.isEnded
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
                    }

                    override fun onNext(t: Response<Any>) {
                        if (t.code === 0) {
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

    fun unJoin() {
        api.unjoinFromEvent(lastEvent.id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : Subscriber<Response<Any>>() {
                    override fun onNext(t: Response<Any>) {
                        if (t.code === 0) {
                            lastEvent.nowPeople -= 1
                            lastEvent.isJoined = false
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
                    }

                    override fun onNext(t: Response<Any>) {
                        if (t.code === 0) {
                            view?.showToast("Вы приняли на себя задачу : " + task.message)
                            val tasks = lastEventTasks
                            val changedTask = tasks?.find { it.id == task.id }
                            if (changedTask != null) {
                                val index = tasks?.indexOf(changedTask)!!.or(0)
                                tasks?.remove(changedTask)
                                val activeUser = SocialNetworkManager.instance.activeUser
                                val newUser = User("", activeUser.id, 0, activeUser.name, activeUser.avatar)
                                tasks?.add(index, changedTask.copy(user = newUser)) //TODO inject social network manager
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
        if (requestCode === REQUEST_CODE) {
            locationManager.update(false)
        }
    }

    companion object {
        @JvmStatic private val REQUEST_CODE = 1002

        @JvmStatic private val MAX_ZOOM = 17f
        @JvmStatic private val MIN_ZOOM = 10f

    }


}