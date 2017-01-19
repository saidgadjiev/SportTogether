package ru.mail.sporttogether.mvp.views.map

import com.google.android.gms.maps.GoogleMap
import ru.mail.sporttogether.mvp.views.IView
import ru.mail.sporttogether.net.models.Event
import ru.mail.sporttogether.net.models.Task
import java.util.*

/**
 * Created by bagrusss on 01.10.16.
 *
 */
interface IMapView : IView, GoogleMap.OnCameraIdleListener {
    fun startAddEventActivity(lng: Double, lat: Double)

    fun finishView()

    fun hideInfo()

    fun showInfo(event: Event, isCancelable: Boolean, tasks: ArrayList<Task>?)

    fun render(event: Event, isCancelable: Boolean, tasks: ArrayList<Task>?)

    fun shareResults()

    fun loadEvents(events: MutableList<Event>)

    fun addEvent(event: Event)

    fun onFinishLoadTasks(tasks: ArrayList<Task>?)

    fun updateAddress(address: String)

    fun onLocationNotChecked()
}