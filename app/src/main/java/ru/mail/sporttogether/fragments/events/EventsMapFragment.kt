package ru.mail.sporttogether.fragments.events

import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.MapView
import ru.mail.sporttogether.activities.AddEventActivity
import ru.mail.sporttogether.databinding.EventsMapBinding
import ru.mail.sporttogether.fragments.PresenterFragment
import ru.mail.sporttogether.mvp.presenters.map.IMapPresenter
import ru.mail.sporttogether.mvp.presenters.map.MapPresenterImpl
import ru.mail.sporttogether.mvp.views.map.IMapView
import ru.mail.sporttogether.net.models.Event

/**
 * Created by bagrusss on 08.10.16.
 * здесь будут вкладки
 */
class EventsMapFragment :
        PresenterFragment<IMapPresenter>(),
        IMapView {

    private lateinit var mapView: MapView
    private lateinit var binding: EventsMapBinding
    private lateinit var bottomSheet: BottomSheetBehavior<View>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = EventsMapBinding.inflate(inflater, container, false)
        mapView = binding.mapview
        mapView.onCreate(savedInstanceState)
        presenter = MapPresenterImpl(this)
        mapView.getMapAsync(presenter)
        bottomSheet = BottomSheetBehavior.from(binding.bottomSheet)
        bottomSheet.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {

            }

        })
        hideInfo()
        return binding.root
    }

    override fun showFab() {
        binding.joinFab.visibility = View.VISIBLE
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
        binding.listener = presenter
        presenter.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
        binding.listener = null
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

    override fun startAddEventActivity(lng: Double, lat: Double) {
        AddEventActivity.start(context, lng, lat)
    }

    override fun finishView() {
        activity.onBackPressed()
    }

    override fun hideInfo() {
        bottomSheet.isHideable = true
        bottomSheet.state = BottomSheetBehavior.STATE_HIDDEN
        binding.joinFab.hide()
    }

    override fun showInfor(event: Event) {
        bottomSheet.isHideable = false
        bottomSheet.state = BottomSheetBehavior.STATE_COLLAPSED
        binding.joinFab.show()
    }

}