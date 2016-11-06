package ru.mail.sporttogether.mvp.presenters.map

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import ru.mail.sporttogether.data.binding.event.EventDetailsListener
import ru.mail.sporttogether.data.binding.event.EventListener
import ru.mail.sporttogether.mvp.presenters.IPresenter

/**
 * Created by bagrusss on 01.10.16.
 * this interface used for two or more implementers for unit tests
 */
interface IMapPresenter :
        IPresenter,
        OnMapReadyCallback,
        EventDetailsListener,
        EventListener,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnCameraMoveStartedListener {

    fun onCameraIdle(x: Int, y: Int)

}