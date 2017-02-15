package ru.mail.sporttogether.fragments.presenter

import android.graphics.Point
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import ru.mail.sporttogether.app.App
import ru.mail.sporttogether.fragments.view.SelectAddressView
import ru.mail.sporttogether.net.models.Event
import ru.mail.sporttogether.net.models.yandex.maps.GeoObject
import java.util.*

/**
 * Created by bagrusss on 12.02.17
 */
class SelectAddressFragmentPresenterImpl(var view: SelectAddressView?) : SelectAddressFragmentPresenter() {

    init {
        App.injector.usePresenterComponent().inject(this)
    }

    override fun onStart() {
        super.onStart()
        map?.let {
            it.setOnCameraIdleListener(view)
        }
    }

    override fun onStop() {
        super.onStop()
        map?.setOnCameraIdleListener(null)

    }

    override fun onDestroy() {
        view = null
        super.onDestroy()
    }

    override fun updateLocation(lat: Double, lng: Double) {
        if (eventsManager.getCreatingEvent() == null) {
            val currentEvent = Event()
            currentEvent.lng = lng
            currentEvent.lat = lat
            eventsManager.setCreatingEvent(currentEvent)
        }
    }

    override fun onMapReady(mapa: GoogleMap) {
        super.onMapReady(mapa)
        eventsManager.getCreatingEvent()?.let {
            mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(it.lat, it.lng), 17f))
            mapa.setOnCameraIdleListener(view)
        }
    }


    override fun getUserImgUrl() = socialNetworkManager.activeUser?.avatar ?: ""

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
            loadAddressFromYandex(lastPos.latitude, lastPos.longitude)
        }
    }

    override fun saveAddress(address: String) {
        eventsManager.getCreatingEvent()?.let {
            it.address = address
            view?.onAddressSaved()
        }

    }

    override fun onAddressError(e: Throwable) {
        view?.updateAddress("")
    }

    override fun onAddressLoaded(geoObjects: ArrayList<GeoObject>) {
        eventsManager.getCreatingEvent()?.let {
            it.lng = lastPos.longitude
            it.lat = lastPos.latitude
            if (geoObjects.isNotEmpty())
                view?.updateAddress(geoObjects[0].textAddress)
        }
    }

}