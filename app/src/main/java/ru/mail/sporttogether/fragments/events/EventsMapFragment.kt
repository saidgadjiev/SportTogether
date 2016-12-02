package ru.mail.sporttogether.fragments.events

import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import com.google.android.gms.maps.MapView
import ru.mail.sporttogether.R
import ru.mail.sporttogether.activities.AddEventActivity
import ru.mail.sporttogether.activities.PresenterActivity
import ru.mail.sporttogether.adapter.TaskAdapter
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
//    @Inject lateinit var socialNetworkManager: SocialNetworkManager

    private lateinit var mapView: MapView
    private lateinit var binding: EventsMapBinding
    private lateinit var bottomSheet: BottomSheetBehavior<View>
    private val data = EventDetailsData()
    private var tasksDialog: TasksDialog? = null

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
        tasksDialog = null
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

    override fun showInfo(event: Event, isCancelable: Boolean) {
        render(event, isCancelable)
        bottomSheet.state = BottomSheetBehavior.STATE_COLLAPSED
        if (event.tasks != null) {
            initTasks(event.tasks!!)
            binding.showTasksBtn.setOnClickListener {
                tasksDialog!!.dialog.show()
            }
        }
    }

    override fun render(event: Event, isCancelable: Boolean) {
        data.category.set(event.category.name)
        data.name.set(event.name)
        data.date.set(SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault()).format(Date(event.date)))
        data.description.set(event.description)
        data.withDescription.set(event.description.isNotEmpty())
        data.isReported.set(event.isReported)
        data.isJoined.set(event.isJoined)
        data.tasksInfo.set(event.tasksInfo())
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
        if (event.tasks != null) {
            //в адаптере хранится ссылка на массив тасков, с ним синхронизируется
            tasksDialog?.taskAdapter?.swapTasks()
        }
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

    override fun checkTask(task: Task) {
        presenter.checkTask(task)
    }

    override fun uncheckTask(task: Task) {
        presenter.uncheckTask(task)
    }

    fun initTasks (tasks: ArrayList<Task>) {
        val tasksBinding: ShowingTasksBinding = ShowingTasksBinding.inflate(LayoutInflater.from(this.context), null, false)
        val dialog: AlertDialog = AlertDialog.Builder(this.context).create()
        val taskAdapter = TaskAdapter(tasks, this, SocialNetworkManager.instance.activeUser.id) // TODO inject manager
        tasksBinding.tasksRecyclerView.adapter = taskAdapter
        tasksBinding.tasksRecyclerView.layoutManager = LinearLayoutManager(this.context)
        tasksDialog = TasksDialog(tasksBinding, taskAdapter, dialog)
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