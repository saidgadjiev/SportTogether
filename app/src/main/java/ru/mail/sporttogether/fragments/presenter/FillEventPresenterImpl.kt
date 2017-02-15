package ru.mail.sporttogether.fragments.presenter

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import ru.mail.sporttogether.app.App
import ru.mail.sporttogether.fragments.view.FillEventView
import ru.mail.sporttogether.net.models.yandex.maps.GeoObject

/**
 * Created by bagrusss on 14.02.17
 */
class FillEventPresenterImpl(var view: FillEventView?) : FillEventPresenter() {

    init {
        App.injector.usePresenterComponent().inject(this)
    }


    override fun onDestroy() {
        view = null
        super.onDestroy()
    }

    override fun onMapReady(mapa: GoogleMap) {
        super.onMapReady(mapa)
        val event = eventsManager.getCreatingEvent()
        event?.let {
            val latlng = LatLng(it.lat, it.lng)
            mapa.moveCamera(CameraUpdateFactory.newLatLng(latlng))
            mapa.addMarker(MarkerOptions().position(latlng))
            view?.updateAddress(it.address)
        }
    }

    override fun fillEvent(name: String,
                           sport: String,
                           maxPeople: Int,
                           description: String,
                           time: Long,
                           joinToEvent: Boolean,
                           needAddTemplate: Boolean) {
        val event = eventsManager.getCreatingEvent()
        event?.let {
            it.name = name
            it.category.name = sport
            it.maxPeople = maxPeople
            it.description = description
            it.date = time
            it.isJoined = joinToEvent
            it.isEnded = needAddTemplate //нужно для создания события на 3 этапе
        }
    }



}