package ru.mail.sporttogether.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.mail.sporttogether.data.binding.FillEventData
import ru.mail.sporttogether.databinding.FragmentFillEventBinding
import ru.mail.sporttogether.fragments.presenter.FillEventPresenter
import ru.mail.sporttogether.fragments.presenter.FillEventPresenterImpl
import ru.mail.sporttogether.fragments.view.FillEventView

/**
 * Created by bagrusss on 14.02.17
 */
class FillEventFragment : AbstractMapFragment<FillEventPresenter>(), FillEventView {

    private lateinit var binding: FragmentFillEventBinding
    private val data = FillEventData()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFillEventBinding.inflate(inflater, container, false)
        binding.addEventData = data

        presenter = FillEventPresenterImpl(this)

        mapView = binding.mapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(presenter)
        return binding.root
    }

    override fun updateAddress(address: String) {
        data.address.set(address)
    }


}