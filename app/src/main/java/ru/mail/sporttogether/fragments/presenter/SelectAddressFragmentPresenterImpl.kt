package ru.mail.sporttogether.fragments.presenter

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import ru.mail.sporttogether.app.App
import ru.mail.sporttogether.auth.core.SocialNetworkManager
import ru.mail.sporttogether.fragments.view.SelectAddressView
import ru.mail.sporttogether.managers.EventsManager
import ru.mail.sporttogether.net.models.Event
import javax.inject.Inject

/**
 * Created by bagrusss on 12.02.17
 */
class SelectAddressFragmentPresenterImpl(var view: SelectAddressView?) : SelectAddressFragmentPresenter() {


    @Inject lateinit var socialNetworkManager: SocialNetworkManager
    @Inject lateinit var eventManager: EventsManager

    private lateinit var currentEvent: Event

    init {
        App.injector.usePresenterComponent().inject(this)
    }

    override fun onDestroy() {
        view = null
        super.onDestroy()
    }


    override fun updateLocation(lat: Double, lng: Double) {
        if (eventManager.getCreatingEvent() == null) {
            currentEvent = Event()
            currentEvent.lng = lng
            currentEvent.lat = lat
            eventManager.setCreatingEvent(currentEvent)
        }
    }

    override fun onMapReady(mapa: GoogleMap) {
        super.onMapReady(mapa)
        eventManager.getCreatingEvent()?.let {
            mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(it.lat, it.lng), 17f))
        }
    }

    override fun getUserImgUrl() = socialNetworkManager.activeUser?.avatar ?: ""

}