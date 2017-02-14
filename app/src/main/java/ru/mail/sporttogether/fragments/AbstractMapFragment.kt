package ru.mail.sporttogether.fragments

import android.os.Bundle
import com.google.android.gms.maps.MapView
import ru.mail.sporttogether.mvp.IPresenter
import ru.mail.sporttogether.mvp.MapPresenter
import ru.mail.sporttogether.mvp.PresenterFragment

/**
 * Created by bagrusss on 13.02.17
 */
abstract class AbstractMapFragment<T : MapPresenter> :
        PresenterFragment<T>() {
    protected lateinit var mapView: MapView

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onStop() {
        mapView.onStop()
        super.onStop()
    }

    override fun onPause() {
        mapView.onPause()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mapView.onDestroy()
    }
}