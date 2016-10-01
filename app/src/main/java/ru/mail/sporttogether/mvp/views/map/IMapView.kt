package ru.mail.sporttogether.mvp.views.map

import ru.mail.sporttogether.mvp.views.IView

/**
 * Created by bagrusss on 01.10.16.
 *
 */
interface IMapView : IView {
    fun startAddEventActivity(lng: Double, lat: Double)
}