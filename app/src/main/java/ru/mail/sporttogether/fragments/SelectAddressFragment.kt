package ru.mail.sporttogether.fragments

import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.*
import ru.mail.sporttogether.R
import ru.mail.sporttogether.activities.NewAddActivity
import ru.mail.sporttogether.data.binding.SelectAddressData
import ru.mail.sporttogether.data.binding.ToolbarWithButtonData
import ru.mail.sporttogether.data.binding.ToolbarWithButtonListener
import ru.mail.sporttogether.databinding.FragmentSelectPlaceBinding
import ru.mail.sporttogether.fragments.presenter.SelectAddressFragmentPresenter
import ru.mail.sporttogether.fragments.presenter.SelectAddressFragmentPresenterImpl
import ru.mail.sporttogether.fragments.view.SelectAddressView

/**
 * Created by bagrusss on 12.02.17
 */
class SelectAddressFragment :
        AbstractMapFragment<SelectAddressFragmentPresenter>(),
        SelectAddressView,
        ToolbarWithButtonListener {

    private lateinit var binding: FragmentSelectPlaceBinding
    private val data = SelectAddressData()
    private val toolbarData = ToolbarWithButtonData()
    private var lng = 0.0
    private var lat = 0.0

    private var markerDownX = 0
    private var markerDownY = 0

    private lateinit var toolbar: Toolbar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = FragmentSelectPlaceBinding.inflate(inflater, container, false)
        binding.data = data
        binding.toolbarData = toolbarData

        val act = activity
        if (act is NewAddActivity) {
            toolbar = binding.include.toolbar
            act.setSupportActionBar(toolbar)
            act.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            toolbar.title = getString(R.string.address)
            toolbarData.buttonText.set(getString(R.string.next))
        }

        presenter = SelectAddressFragmentPresenterImpl(this)


        mapView = binding.mapView
        mapView.onCreate(savedInstanceState)
        data.userImage.set(presenter.getUserImgUrl())
        arguments?.let {
            lat = it.getDouble(KEY_LAT)
            lng = it.getDouble(KEY_LNG)
            presenter.updateLocation(lat, lng)
        }

        mapView.getMapAsync(presenter)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val viewTreeObserver = binding.pinUser.viewTreeObserver
        if (viewTreeObserver.isAlive) {
            viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    initMarkerCoordinates()
                }
            })
        }
    }

    override fun onStart() {
        super.onStart()
        binding.toolbarListener = this
        binding.zoomListener = presenter
    }

    override fun onStop() {
        super.onStop()
        binding.toolbarListener = null
        binding.zoomListener = null
    }

    override fun onAddressSaved() {
        val act = activity
        if (isAdded && act is NewAddActivity) {
            act.nextStep()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            val act = activity
            if (isAdded && act is NewAddActivity) {
                act.onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun initMarkerCoordinates() {
        val array: IntArray = IntArray(2)
        val view = binding.pinUser
        view.getLocationOnScreen(array)

        //костыль, но ширирна определяется правильно
        val display = (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        val size = Point()
        display.getSize(size)
        markerDownX = size.x / 2

        val toolbarHeight = (activity as AppCompatActivity).supportActionBar?.height ?: 0
        markerDownY = array[1] + view.height - statusBarHeight - toolbarHeight
    }

    override fun onCameraIdle() {
        presenter.onCameraIdle(markerDownX, markerDownY)
        data.address.set("")
        data.isAddressLoading.set(true)
        toolbarData.buttonIsVisible.set(false)
    }

    override fun updateAddress(address: String) {
        data.address.set(address)
        data.isAddressLoading.set(false)
        toolbarData.buttonIsVisible.set(true)
    }

    override fun onNextClicked() {
        presenter.saveAddress(data.address.get())
    }

    companion object {

        @JvmStatic private val KEY_LAT = "KEY_LAT"
        @JvmStatic private val KEY_LNG = "KEY_LNG"

        @JvmStatic
        fun newInstance(lat: Double, lng: Double): SelectAddressFragment {
            val fragment = SelectAddressFragment()
            val args = Bundle()
            args.putDouble(KEY_LAT, lat)
            args.putDouble(KEY_LNG, lng)
            fragment.arguments = args
            return fragment
        }
    }
}