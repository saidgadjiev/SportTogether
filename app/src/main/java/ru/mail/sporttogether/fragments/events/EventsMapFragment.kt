package ru.mail.sporttogether.fragments.events

import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.*
import com.google.android.gms.maps.MapView
import ru.mail.sporttogether.R
import ru.mail.sporttogether.activities.AddEventActivity
import ru.mail.sporttogether.activities.PresenterActivity
import ru.mail.sporttogether.adapter.TaskAdapter
import ru.mail.sporttogether.adapter.events.EventsAdapter
import ru.mail.sporttogether.app.App
import ru.mail.sporttogether.auth.core.SocialNetworkManager
import ru.mail.sporttogether.data.binding.event.EventDetailsData
import ru.mail.sporttogether.databinding.EventsMapBinding
import ru.mail.sporttogether.databinding.ShowingTasksBinding
import ru.mail.sporttogether.fragments.CheckingTasks
import ru.mail.sporttogether.fragments.PresenterFragment
import ru.mail.sporttogether.mvp.presenters.map.IMapPresenter
import ru.mail.sporttogether.mvp.presenters.map.MapPresenterImpl
import ru.mail.sporttogether.mvp.views.map.IMapView
import ru.mail.sporttogether.net.models.Event
import ru.mail.sporttogether.net.models.Task
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by bagrusss on 08.10.16.
 * здесь будут вкладки
 */
class EventsMapFragment :
        PresenterFragment<IMapPresenter>(),
        IMapView,
        CheckingTasks {

    private val socialNetworkManager = SocialNetworkManager.instance

    private lateinit var mapView: MapView
    private lateinit var binding: EventsMapBinding
    private lateinit var bottomSheet: BottomSheetBehavior<View>
    private lateinit var bottomSheetEventsList: BottomSheetBehavior<View>
    private val data = EventDetailsData()
    private var tasksDialog: TasksDialog? = null

    private lateinit var eventsListView: RecyclerView
    private val adapter = EventsAdapter()

//    private val tasksDialog

    private var markerDownX = 0
    private var markerDownY = 0
    private var tabHeight = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        App.injector
                .useViewComponent()
                .inject(this)

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


        bottomSheetEventsList = BottomSheetBehavior.from(binding.eventsListSheet)
        bottomSheet.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {

                }

            }
        })

        eventsListView = binding.eventsListRecyclerView
        eventsListView.layoutManager = LinearLayoutManager(context)
        eventsListView.adapter = adapter

        hideInfo()
        setHasOptionsMenu(true)

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
        val name = data.name.get()
        val text = if (data.result.get() == null) data.description.get() else data.result.get()
        socialNetworkManager.getSocialNetwork(socialNetworkManager.getNetworkID())?.sharePost(
                activity,
                name,
                text,
                "http://vk.com"
        )
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
        tasksDialog = null
    }

    override fun loadEvents(events: MutableList<Event>) {
        adapter.swap(events)
    }

    override fun addEvent(event: Event) {
        //adapter.addEvent(event)
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
        tasksDialog = null
    }

    override fun showInfo(event: Event, isCancelable: Boolean, tasks: ArrayList<Task>?) {
        render(event, isCancelable, tasks)
        presenter.loadTasks()
        bottomSheet.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    override fun render(event: Event, isCancelable: Boolean, tasks: ArrayList<Task>?) {
        data.category.set(event.category.name)
        data.name.set(event.name)
        data.date.set(SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault()).format(Date(event.date)))
        data.description.set(event.description)
        data.withDescription.set(event.description.isNotEmpty())
        data.isReported.set(event.isReported)
        data.isJoined.set(event.isJoined)
        if (event.result != null) {
            data.isEnded.set(event.result!!.isNotEmpty())
            data.result.set(event.result!!)
        } else {
            data.isEnded.set(false)
            data.result.set("")
        }
        val people = getString(R.string.users, event.nowPeople, event.maxPeople)
        data.peopleCount.set(people)
        data.showCancelButton.set(isCancelable)
        if (tasks != null) {
            //в адаптере хранится ссылка на массив тасков, с ним синхронизируется
            data.tasksInfo.set(Event.tasksInfo(tasks))
            data.isTasksReady.set(true)
            data.isTasksCanBeChanged.set(data.isTasksReady.get() && event.isJoined && !event.isEnded)
            if (event.isEnded) {
                data.tasksMessage.set("событие уже завершено")
            } else if (!event.isJoined) {
                data.tasksMessage.set("вам нужно присоединиться")
            } else {
                data.tasksMessage.set("")
            }
            tasksDialog?.taskAdapter?.swapTasks()
        } else {
            data.tasksMessage.set("идет загрузка задач")
            data.isTasksReady.set(false)
            data.isTasksCanBeChanged.set(false)
        }
        Log.d("#MY " + javaClass.simpleName, "isTasksCanBeChanged : " + data.isTasksCanBeChanged.get())
    }

    override fun onFinishLoadTasks(tasks: ArrayList<Task>?) {
        if (tasks != null) {
            initTasks(tasks)
            binding.showTasksBtn.setOnClickListener {
                if (data.isTasksCanBeChanged.get())
                    tasksDialog!!.dialog.show()
            }
        }
    }

    override fun checkTask(task: Task) {
        presenter.checkTask(task)
    }

    override fun uncheckTask(task: Task) {
        presenter.uncheckTask(task)
    }

    fun initTasks(tasks: ArrayList<Task>) {
        val tasksBinding: ShowingTasksBinding = ShowingTasksBinding.inflate(LayoutInflater.from(this.context), null, false)
        val dialog: AlertDialog = AlertDialog.Builder(this.context).create()
        val myId = SocialNetworkManager.instance.activeUser.id
        val taskAdapter = TaskAdapter(tasks, this, myId) // TODO inject manager
        tasksBinding.tasksRecyclerView.adapter = taskAdapter
        tasksBinding.tasksRecyclerView.layoutManager = LinearLayoutManager(this.context)
        tasksDialog = TasksDialog(tasksBinding, taskAdapter, dialog)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main, menu)

        val myActionMenuItem = menu.findItem(R.id.action_search)
        val searchView = myActionMenuItem.actionView as SearchView
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
            bottomSheetEventsList.state = BottomSheetBehavior.STATE_EXPANDED
        })
    }

    class TasksDialog(
            val binding: ShowingTasksBinding,
            val taskAdapter: TaskAdapter,
            val dialog: AlertDialog) {
        init {
            this.dialog.setView(this.binding.root)
            this.binding.tasksOkBtn.setOnClickListener {
                this.dialog.hide()
            }
        }
    }
}