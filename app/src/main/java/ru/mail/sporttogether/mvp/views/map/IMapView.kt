package ru.mail.sporttogether.mvp.views.map

import com.google.android.gms.maps.GoogleMap
import ru.mail.sporttogether.mvp.views.IView
import ru.mail.sporttogether.net.models.Event

/**
 * Created by bagrusss on 01.10.16.
 *
 */
interface IMapView : IView, GoogleMap.OnCameraIdleListener {
    fun startAddEventActivity(lng: Double, lat: Double)

    fun finishView()

    fun hideInfo()

    fun showInfo(event: Event, isCancelable: Boolean)

    fun render(event: Event, isCancelable: Boolean)

    fun shareResults()

    fun loadEvents(events: MutableList<Event>)

    fun addEvent(event: Event)
}