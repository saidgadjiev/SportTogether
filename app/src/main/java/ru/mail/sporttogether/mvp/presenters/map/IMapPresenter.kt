package ru.mail.sporttogether.mvp.presenters.map

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import ru.mail.sporttogether.data.binding.ZoomListener
import ru.mail.sporttogether.fragments.CheckingTasks
import ru.mail.sporttogether.mvp.presenters.IPresenter
import ru.mail.sporttogether.net.models.Event

/**
 * Created by bagrusss on 01.10.16.
 * this interface used for two or more implementers for unit tests
 */
interface IMapPresenter :
        IPresenter,
        OnMapReadyCallback,
        CheckingTasks,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMarkerClickListener,
        ZoomListener{

    fun onCameraIdle(x: Int, y: Int)

    fun loadEvents()

    fun searchByCategory(s: String)

    fun loadTasks(event: Event)

    fun fabClicked(isBottomSheet: Boolean = false)

    fun cancelEvent()

    fun doJoin()

    fun doAngry()

}