package ru.mail.sporttogether.fragments.events

import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.MapView
import ru.mail.sporttogether.R
import ru.mail.sporttogether.activities.AddEventActivity
import ru.mail.sporttogether.data.binding.event.EventDetailsData
import ru.mail.sporttogether.databinding.EventsMapBinding
import ru.mail.sporttogether.fragments.PresenterFragment
import ru.mail.sporttogether.mvp.presenters.map.IMapPresenter
import ru.mail.sporttogether.mvp.presenters.map.MapPresenterImpl
import ru.mail.sporttogether.mvp.views.map.IMapView
import ru.mail.sporttogether.net.models.Event
import java.text.SimpleDateFormat
import java.util.*

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
    private val data = EventDetailsData()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = EventsMapBinding.inflate(inflater, container, false)
        mapView = binding.mapview
        mapView.onCreate(savedInstanceState)
        presenter = MapPresenterImpl(this)
        mapView.getMapAsync(presenter)
        binding.data = data
        bottomSheet = BottomSheetBehavior.from(binding.bottomSheet)
        bottomSheet.isHideable = true
        bottomSheet.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {

            }

        })
        hideInfo()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        presenter.onStart()
        mapView.onStart()
        binding.listener = presenter
        binding.addListener = presenter
    }

    override fun onStop() {
        super.onStop()
        presenter.onStop()
        mapView.onStop()
        binding.listener = null
        binding.addListener = null
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
        presenter.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()

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
        bottomSheet.state = BottomSheetBehavior.STATE_HIDDEN
    }

    override fun showInfo(event: Event) {
        data.category.set(event.categoryId.toString())
        data.name.set(event.name)
        data.date.set(SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date(event.date)))
        data.description.set(event.description)
        val people = getString(R.string.users, event.nowPeople, event.maxPeople)
        data.peopleCount.set(people)
        bottomSheet.state = BottomSheetBehavior.STATE_COLLAPSED
    }

}