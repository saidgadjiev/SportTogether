package ru.mail.sporttogether.fragments

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.view.MenuItemCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.android.gms.maps.MapView
import kotlinx.android.synthetic.main.events_map.*
import ru.mail.sporttogether.R
import ru.mail.sporttogether.activities.AddEventActivity
import ru.mail.sporttogether.activities.DrawerActivity
import ru.mail.sporttogether.adapter.MapEventsAdapter
import ru.mail.sporttogether.adapter.TaskAdapter
import ru.mail.sporttogether.data.binding.event.ButtonListener
import ru.mail.sporttogether.data.binding.event.EventDetailsData
import ru.mail.sporttogether.data.binding.event.EventDetailsListener
import ru.mail.sporttogether.databinding.EventsMapBinding
import ru.mail.sporttogether.fragments.adapter.EventsSearchAdapter
import ru.mail.sporttogether.fragments.presenter.EventsMapFragmentPresenter
import ru.mail.sporttogether.fragments.presenter.EventsMapFragmentPresenterImpl
import ru.mail.sporttogether.fragments.view.EventsMapView
import ru.mail.sporttogether.mvp.PresenterActivity
import ru.mail.sporttogether.mvp.PresenterFragment
import ru.mail.sporttogether.net.models.Event
import ru.mail.sporttogether.net.models.Task
import ru.mail.sporttogether.utils.DateUtils
import ru.mail.sporttogether.utils.DialogUtils
import ru.mail.sporttogether.utils.ShareUtils
import java.util.*


/**
 * Created by bagrusss on 08.10.16
 */
class EventsMapFragment :
        PresenterFragment<EventsMapFragmentPresenter>(),
        EventsMapView,
        EventDetailsListener,
        ButtonListener,
        CheckingTasks {

    private lateinit var mapView: MapView
    private lateinit var binding: EventsMapBinding
    private lateinit var eventDetailsBottomSheet: BottomSheetBehavior<View>

    private lateinit var resultsContainer: FrameLayout
    private var mapEventsListController: MapEventsListController? = null

    private lateinit var zoomPanel: LinearLayout
    private lateinit var searchView: SearchView

    private lateinit var eventsListView: RecyclerView

    private val adapter = EventsSearchAdapter()
    private var tasksAdapter: TaskAdapter? = null

    private val data = EventDetailsData()

    private var markerDownX = 0
    private var markerDownY = 0
    private var tabHeight = 0
    private var locationDialog: Dialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = EventsMapBinding.inflate(inflater, container, false)
        binding.data = data
        zoomPanel = binding.zoomPanel
        mapView = binding.mapview
        eventDetailsBottomSheet = BottomSheetBehavior.from(binding.bottomSheet)
        eventDetailsBottomSheet.isHideable = true
        eventDetailsBottomSheet.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            var needHideZoom = false
            var needHideList = false
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                if (slideOffset > 0.65 && needHideList) {
                    hideEventsList()
                } else if (slideOffset > 0.4) {
                    presenter.checkZoomForListEvents()
                }

                if (slideOffset > 0.9f && needHideZoom) {
                    zoomPanel.animate().scaleX(0f).scaleY(0f).setDuration(50L).start()
                } else if (slideOffset > 0.75f) {
                    zoomPanel.animate().scaleX(1f).scaleY(1f).setDuration(50L).start()
                }
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                needHideZoom = binding.bottomSheet.height / binding.root.height.toFloat() > 0.85
                needHideList = isEventsListShouldBeHidden()
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        data.fabForBottomSheet.set(false)
                    }
                    else -> {
                        data.fabForBottomSheet.set(true)
                    }
                }
            }

        })

        eventsListView = binding.eventsListRecyclerView
        eventsListView.layoutManager = LinearLayoutManager(context)
        eventsListView.adapter = adapter

        resultsContainer = binding.eventsListSheet
        resultsContainer.pivotY = resultsContainer.height.toFloat()
        resultsContainer.animate().scaleY(0f).setDuration(0L).start()

        mapEventsListController = MapEventsListController()

        mapView.onCreate(savedInstanceState)
        presenter = EventsMapFragmentPresenterImpl(this)
        presenter.onCreate(savedInstanceState)
        mapView.getMapAsync(presenter)

        hideInfo()
        setHasOptionsMenu(true)

        return binding.root
    }

    fun isEventsListShouldBeHidden(): Boolean {

        val fl = binding.bottomSheet.height / binding.root.height.toFloat()
        Log.d(TAG, "fl : " + fl)
        return fl > 0.5

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
        presenter.loadPinImage()
    }

    override fun loadUserPinImage(url: String) {
        Glide.with(context)
                .load(url)
                .into(binding.userPic)
    }

    fun initMarkerCoordinates() {
        val array: IntArray = IntArray(2)
        val view = binding.userPoint
        view.getLocationOnScreen(array)

        //костыль, но ширирна определяется правильно
        val display = (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        val size = Point()
        display.getSize(size)
        markerDownX = size.x / 2

        val toolbarHeight = (activity as PresenterActivity<*>).supportActionBar?.height ?: 0
        markerDownY = array[1] + view.height - statusBarHeight - toolbarHeight - tabHeight
    }

    override fun shareResults(event: Event) {
        ShareUtils.createShareIntent(event, getString(R.string.share))?.let {
            startActivity(it)
        }
    }

    override fun showInfo(event: Event, isCancelable: Boolean, tasks: ArrayList<Task>?) {
        render(event, isCancelable, tasks)
        eventDetailsBottomSheet.peekHeight = binding.include.cardviewHeader.height + binding.include.frameLayout.height
        eventDetailsBottomSheet.state = BottomSheetBehavior.STATE_COLLAPSED
        presenter.loadTasks(event)
        hideResultsList()
        if (!searchView.isIconified) {
            val act = activity
            if (act is DrawerActivity) {
                val toolbar = act.findViewById(R.id.drawer_toolbar)
                if (toolbar is Toolbar) {
                    toolbar.collapseActionView()
                }
            }
        }
    }

    override fun updateAddress(address: String) {
        data.address.set(address)
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
        binding.listener = this
        binding.addListener = this
        binding.zoomListener = presenter
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
        binding.listener = null
        binding.addListener = null
        binding.zoomListener = null
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
        presenter.checkLocation()
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
        tasksAdapter = null
        mapEventsListController = null
    }

    override fun showMap() {
        val activity = activity
        if (isAdded && activity is DrawerActivity) {
            activity.showMap()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK)
                    data?.let {
                        it.getParcelableExtra<Event>(AddEventActivity.KEY_EVENT)?.let {
                            setBottomSheetCollapsed()
                            showInfo(it, true, it.tasks)
                        }
                    }
            }
            REQUEST_LOCATION_CODE -> {
                if (resultCode == 0) {
                    presenter.onLocationEnabled()
                }
            }
            REQUEST_PERMISSIONS_CODE -> {

            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_PERMISSIONS_CODE) {
            var isEverythingAllowed = true
            for (result in grantResults) {
                isEverythingAllowed = isEverythingAllowed and (result == PackageManager.PERMISSION_GRANTED)
            }

            if (isEverythingAllowed) {
                presenter.onPermissionsGranted(requestCode)
            } else {
                presenter.onPermissionNotGranted(requestCode)
            }
        }
    }

    override fun checkLocationPermissions(permissions: Array<String>) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            requestPermissions(permissions, REQUEST_PERMISSIONS_CODE)
    }

    override fun onAngryButtonClicked() {
        DialogUtils.showAngryDialog(context, {
            presenter.doAngry()
        }, {

        })
    }

    override fun onJoinButtonClicked() {
        presenter.doJoin()
    }

    override fun onCancelButtonClicked() {
        presenter.cancelEvent()
    }

    override fun loadEvents(events: MutableList<Event>) {
        adapter.swap(events)
    }

    override fun onCameraIdle() {
        presenter.onCameraIdle(markerDownX, markerDownY)
    }

    override fun startAddEventActivity(lng: Double, lat: Double) {
        val event = Event(lat = lat, lng = lng)
        AddEventActivity.startForResult(this, event, REQUEST_CODE)
    }

    override fun finishView() {
        activity.onBackPressed()
    }

    override fun hideInfo() {
        eventDetailsBottomSheet.state = BottomSheetBehavior.STATE_HIDDEN
    }

    override fun onButtonClicked() {
        presenter.fabClicked(data.fabForBottomSheet.get())
    }

    override fun render(event: Event, isCancelable: Boolean, tasks: ArrayList<Task>?) {
        renderBaseInfo(event)
        renderResult(event)
        data.showCancelButton.set(isCancelable)
        renderTasks(event, tasks)
    }

    override fun setBottomSheetCollapsed() {
        eventDetailsBottomSheet.peekHeight = binding.include.cardviewHeader.height + binding.include.frameLayout.height
        eventDetailsBottomSheet.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun renderBaseInfo(event: Event) {
        data.category.set(event.category.name)
        data.address.set(event.address)
        data.name.set(event.name)
        data.date.set(DateUtils.toXLongDateString(Date(event.date)))
        data.description.set(event.result ?: event.description)
        Glide.with(this).load(event.user.avatar).asBitmap().placeholder(R.drawable.ic_person).into(binding.include.eventCreatorAvatar)
        data.creatorName.set(event.user.name)
//        binding.include.descriptionText.movementMethod = ScrollingMovementMethod()
        data.isReported.set(event.isReported)
        data.isJoined.set(event.isJoined)
        val people = getString(R.string.users, event.nowPeople, event.maxPeople)
        data.peopleCount.set(people)
    }

    private fun renderResult(event: Event) {
        if (event.result != null) {
            data.isEnded.set(event.result!!.isNotEmpty())
            data.result.set(event.result!!)
        } else {
            data.isEnded.set(false)
            data.result.set("")
        }
    }

    private fun renderTasks(event: Event, tasks: ArrayList<Task>?) {
        if (tasks != null) {
            data.tasksInfo.set(Event.tasksInfo(tasks))
            val withTasks = tasks.size > 0
            data.withTasks.set(withTasks)
            data.isTasksCanBeChanged.set(withTasks && event.isJoined && !event.isEnded)
            if (event.isEnded) {
                data.tasksMessage.set("событие уже завершено")
            } else if (!event.isJoined) {
                data.tasksMessage.set("вам нужно присоединиться")
            } else {
                data.tasksMessage.set("")
            }
            tasksAdapter?.swapTasks()
        } else {
            data.tasksMessage.set("идет загрузка задач")
            data.withTasks.set(false)
            data.isTasksCanBeChanged.set(false)
        }
    }

    override fun onFinishLoadTasks(tasks: ArrayList<Task>?) {
        initTasks(tasks ?: ArrayList(5))
    }

    override fun checkTask(task: Task) {
        presenter.checkTask(task)
    }

    override fun uncheckTask(task: Task) {
        presenter.uncheckTask(task)
    }

    //TODO исправить !!!
    fun initTasks(tasks: ArrayList<Task>) {
        val myId = presenter.getMyId()
        tasksAdapter = TaskAdapter(tasks, this, myId)
        binding.include.tasksListRecyclerView.adapter = tasksAdapter
        binding.include.tasksListRecyclerView.layoutManager = LinearLayoutManager(this.context)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main, menu)

        val myActionMenuItem = menu.findItem(R.id.action_search)
        MenuItemCompat.setOnActionExpandListener(myActionMenuItem, object : MenuItemCompat.OnActionExpandListener {
            override fun onMenuItemActionExpand(p0: MenuItem): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(p0: MenuItem): Boolean {
                hideResultsList()
                return true
            }

        })
        searchView = myActionMenuItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                presenter.searchByCategory(query)

                return false
            }

            override fun onQueryTextChange(s: String): Boolean {
                return false
            }
        })
        searchView.setOnSearchClickListener({ view ->
            showResultsList()
            hideInfo()
        })
    }

    private fun showResultsList() {
        resultsContainer.visibility = View.VISIBLE
        resultsContainer.animate().scaleY(1f).setDuration(300L).start()
        fab.animate().scaleX(0f).scaleY(0f).setDuration(300L).start()
    }

    private fun hideResultsList() {
        val animator = resultsContainer.animate().scaleY(0f).setDuration(300L).withEndAction {
            resultsContainer.visibility = View.GONE
        }
        fab.animate().scaleX(1f).scaleY(1f).setDuration(300L).start()
        animator.start()
    }

    override fun showEventsList() {
        mapEventsListController?.show()
    }

    override fun renderEventsList(events: MutableList<Event>) {
        mapEventsListController?.render(events)
//        updateMapEventsRecyclerView(events)

    }

    override fun hideEventsList() {
        mapEventsListController?.hide()
    }

    override fun onLocationNotChecked() {
        if (locationDialog == null) {
            locationDialog = AlertDialog.Builder(context)
                    .setMessage(R.string.location_disabled_message)
                    .setTitle(R.string.title_location_access)
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.ok, { dialog, which ->
                        startActivityForResult(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), REQUEST_LOCATION_CODE)
                        locationDialog = null
                    })
                    .setNegativeButton(android.R.string.cancel, { dialog, which ->
                        Toast.makeText(context, R.string.cannot_find_you, Toast.LENGTH_LONG).show()
                        locationDialog = null
                    })
                    .create()
            locationDialog!!.show()
        }
    }

    inner class MapEventsListController {
        private var mapEventsAdapter: MapEventsAdapter? = null
        private var mapEventsLayout: ViewGroup? = null
        private var mapEventsRecyclerView: RecyclerView? = null
        var isShowed: Boolean = false
        var isBottomSheetOpened: Boolean = false
        var wasRendered: Boolean = false

        init {
        }

        fun show() {
            if (isShowed) return
            if (mapEventsLayout == null) {
                mapEventsLayout = binding.mapEventsListInclude.mapEventsLayout
            }
            if (mapEventsLayout!!.visibility != View.VISIBLE) {
                Log.d(TAG, "show events list animation start")
                mapEventsLayout!!.visibility = View.VISIBLE
                mapEventsLayout!!.animate().scaleX(1f).setDuration(200L).withEndAction {
                    isShowed = true
                }.start()
            }
        }

        fun hide() {
            if (!isShowed) return
            if (mapEventsLayout == null) {
                mapEventsLayout = binding.mapEventsListInclude.mapEventsLayout
            }
            if (mapEventsLayout!!.visibility != View.GONE) {
                mapEventsLayout!!.animate().scaleX(0f).setDuration(200L).withEndAction {
                    mapEventsLayout!!.visibility = View.GONE
                    isShowed = false
                }.start()
                Log.d(TAG, "hide events list animation start")
            }
        }

        fun render(events: MutableList<Event>) {
            if (isShowed || !wasRendered) {
                wasRendered = true
                if (mapEventsRecyclerView == null) {
                    mapEventsRecyclerView = binding.mapEventsListInclude.mapEventsRecyclerView
                    mapEventsRecyclerView!!.layoutManager = LinearLayoutManager(activity)
                }
                if (mapEventsAdapter == null) {
                    mapEventsAdapter = MapEventsAdapter(events, presenter)
                } else {
                    mapEventsAdapter!!.swap(events)
                }
                if (mapEventsRecyclerView!!.adapter == null) {
                    mapEventsRecyclerView!!.adapter = mapEventsAdapter
                }
            }

        }
    }

    companion object {
        @JvmStatic val REQUEST_CODE = 1092
        @JvmStatic val REQUEST_LOCATION_CODE = 1093
        @JvmStatic val REQUEST_PERMISSIONS_CODE = 1094

        val TAG = "#MY " + EventsMapFragment::class.java.simpleName
    }
}