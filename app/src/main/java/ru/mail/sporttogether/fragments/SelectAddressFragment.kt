package ru.mail.sporttogether.fragments

import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    private lateinit var toolbar: Toolbar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSelectPlaceBinding.inflate(inflater, container, false)
        binding.data = data
        binding.toolbarData = toolbarData

        val act = activity
        if (act is NewAddActivity) {
            presenter = SelectAddressFragmentPresenterImpl(this)
            toolbar = binding.include.toolbar
            setupToolbar(toolbar)
            toolbar.title = getString(R.string.address)
        }

        toolbarData.buttonText.set(getString(R.string.next))

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

    override fun onStart() {
        super.onStart()
        binding.toolbarListener = this
    }

    override fun onStop() {
        super.onStop()
        binding.toolbarListener = null
    }

    override fun updateAddress(address: String) {
        data.address.set(address)
        data.isAddressLoading.set(false)
    }

    override fun onNextClicked() {

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