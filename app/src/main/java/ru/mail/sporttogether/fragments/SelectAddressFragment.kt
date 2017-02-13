package ru.mail.sporttogether.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.mail.sporttogether.databinding.FragmentSelectPlaceBinding
import ru.mail.sporttogether.mvp.NewAddEventPresenter
import ru.mail.sporttogether.fragments.view.SelectAddressView

/**
 * Created by bagrusss on 12.02.17
 */
class SelectAddressFragment :
        AbstractMapFragment<NewAddEventPresenter>(),
        SelectAddressView {

    private lateinit var binding: FragmentSelectPlaceBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSelectPlaceBinding.inflate(inflater, container, false)

        return binding.root
    }
}