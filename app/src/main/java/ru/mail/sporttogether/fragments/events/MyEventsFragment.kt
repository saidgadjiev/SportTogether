package ru.mail.sporttogether.fragments.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.mail.sporttogether.databinding.FragmentMyEventsBinding
import ru.mail.sporttogether.fragments.AbstractFragment

class MyEventsFragment : AbstractFragment() {
    private lateinit var binding: FragmentMyEventsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMyEventsBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        fun newInstance() = MyEventsFragment()
    }
}