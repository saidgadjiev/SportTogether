package ru.mail.sporttogether.mvp.views.map

import ru.mail.sporttogether.mvp.views.IView
import ru.mail.sporttogether.net.models.Event

/**
 * Created by bagrusss on 01.10.16.
 *
 */
interface IMapView : IView {
    fun startAddEventActivity(lng: Double, lat: Double)

    fun showFab()

    fun finishView()

    fun hideInfo()

    fun showInfo(event: Event)

}