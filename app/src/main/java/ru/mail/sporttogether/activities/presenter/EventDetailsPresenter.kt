package ru.mail.sporttogether.activities.presenter

import android.os.Bundle
import com.google.android.gms.maps.OnMapReadyCallback
import ru.mail.sporttogether.mvp.IPresenter
import ru.mail.sporttogether.net.models.Event

/**
 * Created by bagrusss on 12.02.17
 */
interface EventDetailsPresenter : IPresenter, OnMapReadyCallback {
    fun loadAddress(lat: Double, lng: Double)
    fun onCreate(e: Event, savedInstanceState: Bundle?)
}