package ru.mail.sporttogether.mvp

import android.support.annotation.CallSuper
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback

/**
 * Created by bagrusss on 14.02.17
 */
abstract class MapPresenter : IPresenter, OnMapReadyCallback {

    protected var map: GoogleMap? = null

    @CallSuper
    override fun onMapReady(mapa: GoogleMap) {
        map = mapa
    }

}