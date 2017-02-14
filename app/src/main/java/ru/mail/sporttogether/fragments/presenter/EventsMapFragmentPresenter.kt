package ru.mail.sporttogether.fragments.presenter

import com.google.android.gms.maps.GoogleMap
import ru.mail.sporttogether.data.binding.ZoomListener
import ru.mail.sporttogether.data.binding.event.GoToMarker
import ru.mail.sporttogether.fragments.CheckingTasks
import ru.mail.sporttogether.mvp.MapPresenter
import ru.mail.sporttogether.net.models.Event

/**
 * Created by bagrusss on 01.10.16.
 * this interface used for two or more implementers for unit tests
 */
abstract class EventsMapFragmentPresenter :
        MapPresenter(),
        CheckingTasks,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMarkerClickListener,
        ZoomListener,
        GoToMarker {

    abstract fun checkLocation()

    abstract fun checkZoomForListEvents()

    abstract fun onLocationEnabled()

    abstract fun onCameraIdle(x: Int, y: Int)

    abstract fun loadEvents()

    abstract fun searchByCategory(s: String)

    abstract fun loadTasks(event: Event)

    abstract fun fabClicked(isBottomSheet: Boolean = false)

    abstract fun cancelEvent()

    abstract fun doJoin()

    abstract fun doAngry()

    abstract fun getMyId(): Long

    abstract fun loadPinImage()


}