package ru.mail.sporttogether.fragments.view

import com.google.android.gms.maps.GoogleMap
import ru.mail.sporttogether.mvp.IView
import ru.mail.sporttogether.net.models.Event
import ru.mail.sporttogether.net.models.Task
import java.util.*

/**
 * Created by bagrusss on 01.10.16
 */
interface EventsMapView : IView, GoogleMap.OnCameraIdleListener {
    fun startAddEventActivity(lng: Double, lat: Double)

    fun finishView()

    fun hideInfo()

    fun showInfo(event: Event, isCancelable: Boolean, tasks: MutableList<Task>?)

    fun render(event: Event, isCancelable: Boolean, tasks: MutableList<Task>?)

    fun setBottomSheetCollapsed()

    fun shareResults(event: Event)

    fun loadEvents(events: MutableList<Event>)

    fun onFinishLoadTasks(tasks: ArrayList<Task>?)

    fun updateAddress(address: String)

    fun onLocationNotChecked()

    fun checkLocationPermissions(permissions: Array<String>)

    fun showMap()

    fun showEventsList()

    fun renderEventsList(events: MutableList<Event>)

    fun hideEventsList()
}