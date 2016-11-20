package ru.mail.sporttogether.fragments.events

import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.view.*
import com.google.android.gms.maps.MapView
import ru.mail.sporttogether.R
import ru.mail.sporttogether.activities.AddEventActivity
import ru.mail.sporttogether.activities.PresenterActivity
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

    private var markerDownX = 0
    private var markerDownY = 0
    private var tabHeight = 0

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val viewTreeObserver = binding.userPoint.viewTreeObserver
        if (viewTreeObserver.isAlive) {
            viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    initMarkerCoordinates()
                }
            })
        }
    }

    fun initMarkerCoordinates() {
        val array: IntArray = IntArray(2)
        val view = binding.userPoint
        view.getLocationOnScreen(array)
        tabHeight = arguments.getInt(TAB_HEIGHT_KEY)
        //markerDownX = array[0] + view.width / 2 //center X

        //костыль, но ширирна определяется правильно
        val display = (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        val size = Point()
        display.getSize(size)
        markerDownX = size.x / 2

        val toolbarHeight = (activity as PresenterActivity<*>).supportActionBar?.height ?: 0
        markerDownY = array[1] + view.height - statusBarHeight - toolbarHeight - tabHeight
    }

    override fun shareResults() {
        val parentActivity = activity
        if (parentActivity is PresenterActivity<*>) {
            parentActivity.shareToSocial(data.name.get(), data.description.get())
        }
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
        binding.listener = presenter
        binding.addListener = presenter
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
        binding.listener = null
        binding.addListener = null
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
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
    }

    override fun onCameraIdle() {
        presenter.onCameraIdle(markerDownX, markerDownY)
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

    override fun showInfo(event: Event, isCancelable: Boolean) {
        render(event, isCancelable)
        bottomSheet.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    override fun render(event: Event, isCancelable: Boolean) {
        data.category.set(event.category.name)
        data.name.set(event.name)
        data.date.set(SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault()).format(Date(event.date)))
        data.description.set(event.description)
        data.isReported.set(event.isReported)
        data.isJoined.set(event.isJoined)
        val people = getString(R.string.users, event.nowPeople, event.maxPeople)
        data.peopleCount.set(people)
        data.showCancelButton.set(isCancelable)
    }

    companion object {
        @JvmStatic val TAB_HEIGHT_KEY = "TAB_HEIGHT"

        @JvmStatic fun newInstance(tabHeight: Int): EventsMapFragment {
            val args = Bundle()
            args.putInt(TAB_HEIGHT_KEY, tabHeight)
            val fragment = EventsMapFragment()
            fragment.arguments = args
            return fragment
        }
    }

}