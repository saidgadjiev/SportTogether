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
import ru.mail.sporttogether.eventbus.PermissionGrantedMessage
import ru.mail.sporttogether.eventbus.PermissionMessage
import ru.mail.sporttogether.managers.LocationManager
import ru.mail.sporttogether.managers.data.CredentialsManager
import ru.mail.sporttogether.managers.events.EventsManager
import ru.mail.sporttogether.mvp.views.map.IMapView
import ru.mail.sporttogether.net.api.EventsAPI
import ru.mail.sporttogether.net.models.Event
import ru.mail.sporttogether.net.models.Task
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

//    @Deprecated("lastEvent object instead of this", replaceWith = ReplaceWith("lastEvent.id"))
//    private var lastEventId = 0L
    private lateinit var lastEvent: Event

    private val markerIdEventMap = HashMap<String, Event>()

    @Inject lateinit var api: EventsAPI
    @Inject lateinit var eventsManager: EventsManager
    @Inject lateinit var locationManager: LocationManager
    @Inject lateinit var creditalsManager: CredentialsManager

    private val userId: Long

    private var apiSubscribtion: Subscription? = null
    private var locationSubscription: Subscription? = null

    private var eventsSubscribion: Subscription? = null

    init {
        App.injector
                .usePresenterComponent()
                .inject(this)
        userId = creditalsManager.getUserData().id
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
        }
    }

    override fun onResume() {
        map?.let {
            it.setOnMapClickListener(this)
            it.setOnMarkerClickListener(this)
        }
    }

    override fun onDestroy() {
        locationSubscription?.unsubscribe()
        view = null
        map?.let {
            with(it) {
                setOnCameraIdleListener(null)
                setOnMapClickListener(null)
                setOnMarkerClickListener(null)
            }
        }
        map = null
    }

    override fun onMapClick(latlng: LatLng) {
        view?.hideInfo()
    }

    override fun onShareButtonClicked() {
        view?.shareResults()
    }

    override fun onCancelButtonClicked() {
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

    override fun onAddButtonClicked() {
        lastPos.let {
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
        val event: Event? = markerIdEventMap[marker.id]
        event?.let {
            view?.hideInfo()
            lastEvent = it
            view?.showInfo(lastEvent, (userId == event.userId) and !event.isEnded)
        }
    }

    private fun render() {
        view?.render(lastEvent, (userId == lastEvent.userId) and !lastEvent.isEnded)
    }

    override fun onBackPressed() {
        view?.finishView()
    }

    override fun onAngryButtonClicked() {
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
                        }
                        else view?.showToast("Вы уже жаловались на это событие")
                    }
                })
    }

    override fun onJoinButtonClicked() {
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
        Log.d("#MY " + javaClass.simpleName, "In presenter : " + task)
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
                            val tasks = lastEvent.tasks
                            val changedTask = tasks?.find { it.id == task.id }
                            if (changedTask != null) {
                                val index = tasks?.indexOf(changedTask)!!.or(0)
                                tasks?.remove(changedTask)
                                tasks?.add(index, changedTask.copy(userId = creditalsManager.getUserData().id) )
                            }
                            render()
                        }
                        else view?.showToast(t.message)
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
                            val tasks = lastEvent.tasks
                            val changedTask = tasks?.find { it.id == task.id }
                            if (changedTask != null) {
                                val index = tasks?.indexOf(changedTask)!!.or(0)
                                tasks?.remove(changedTask)
                                tasks?.add(index, changedTask.copy(userId = null) )
                            }
                            render()
                        }
                        else view?.showToast(t.message)
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