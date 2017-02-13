package ru.mail.sporttogether.fragments

import android.os.Bundle
import com.google.android.gms.maps.MapView
import ru.mail.sporttogether.mvp.IPresenter
import ru.mail.sporttogether.mvp.PresenterFragment

/**
 * Created by bagrusss on 13.02.17
 */
abstract class AbstractMapFragment<T : IPresenter> : PresenterFragment<T>() {
    protected lateinit var mapView: MapView

    override fun onSaveInstanceState(outState: Bundle?) {
        mapView.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onStart() {
        mapView.onStart()
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onResume() {
        mapView.onResume()
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mapView.onDestroy()
    }
}