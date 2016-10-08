package ru.mail.sporttogether.fragments.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.MapView
import ru.mail.sporttogether.activities.AddEventActivity
import ru.mail.sporttogether.data.binding.FabListener
import ru.mail.sporttogether.databinding.ActivityMapBinding
import ru.mail.sporttogether.fragments.AbstractFragment
import ru.mail.sporttogether.mvp.presenters.map.IMapPresenter
import ru.mail.sporttogether.mvp.presenters.map.MapPresenterImpl
import ru.mail.sporttogether.mvp.views.map.IMapView

/**
 * Created by bagrusss on 08.10.16.
 * здесь будут вкладки
 */
class EventsMapFragment : AbstractFragment(), IMapView, FabListener {

    private lateinit var mapView: MapView
    private lateinit var binding: ActivityMapBinding

    private lateinit var presenter: IMapPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = ActivityMapBinding.inflate(inflater, container, false)
        mapView = binding.mapview
        mapView.onCreate(savedInstanceState)
        presenter = MapPresenterImpl(this)
        mapView.getMapAsync(presenter)
        return binding.root
    }

    override fun showFab() {
        binding.fab.visibility = View.VISIBLE
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
        binding.fabListener = this
        presenter.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
        binding.fabListener = null
        presenter.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mapView.onDestroy()
        presenter.onDestroy()
    }

    override fun onFabClicked(v: View) {
        v.isEnabled = false
        presenter.fabClicked()
        v.isEnabled = true
    }

    override fun startAddEventActivity(lng: Double, lat: Double) {
        AddEventActivity.start(context, lng, lat)
    }

}