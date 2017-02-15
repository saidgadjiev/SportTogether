package ru.mail.sporttogether.mvp

import android.support.annotation.CallSuper
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import ru.mail.sporttogether.auth.core.SocialNetworkManager
import ru.mail.sporttogether.data.binding.ZoomListener
import ru.mail.sporttogether.managers.EventsManager
import ru.mail.sporttogether.net.api.YandexMapsApi
import ru.mail.sporttogether.net.models.yandex.maps.GeoObject
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by bagrusss on 14.02.17
 */
abstract class MapPresenter :
        IPresenter,
        ZoomListener,
        OnMapReadyCallback {

    @Inject lateinit var socialNetworkManager: SocialNetworkManager
    @Inject lateinit var eventsManager: EventsManager
    @Inject lateinit var yandexApi: YandexMapsApi

    protected var map: GoogleMap? = null
    protected lateinit var lastPos: LatLng

    @CallSuper
    override fun onMapReady(mapa: GoogleMap) {
        map = mapa
    }

    protected fun loadAddressFromYandex(lat: Double, lng: Double) {
        yandexApi.getAddressByCoordinates(longlat = "$lng,$lat")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : Subscriber<ArrayList<GeoObject>>() {
                    override fun onNext(t: ArrayList<GeoObject>) {
                        onAddressLoaded(t)
                        unsubscribe()
                    }

                    override fun onError(e: Throwable) {
                        onAddressError(e)
                        unsubscribe()
                    }

                    override fun onCompleted() {

                    }

                })
    }

    open fun onAddressError(e: Throwable) {}

    open fun onAddressLoaded(geoObjects: ArrayList<GeoObject>) {}

    open fun onCameraIdle(x: Int, y: Int) {}

    override fun zoomInClicked() {
        map?.animateCamera(CameraUpdateFactory.zoomIn())
    }

    override fun zoomOutClicked() {
        map?.animateCamera(CameraUpdateFactory.zoomOut())
    }

    companion object {
        @JvmStatic val MAX_ZOOM = 17f
        @JvmStatic val MIN_ZOOM = 10f
    }

}