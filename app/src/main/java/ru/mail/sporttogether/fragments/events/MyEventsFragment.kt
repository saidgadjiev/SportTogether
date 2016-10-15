package ru.mail.sporttogether.fragments.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import ru.mail.sporttogether.databinding.FragmentMyEventsBinding
import ru.mail.sporttogether.fragments.PresenterFragment
import ru.mail.sporttogether.mvp.presenters.event.MyEventsPresenter

class MyEventsFragment : PresenterFragment<MyEventsPresenter>() {
    private lateinit var binding: FragmentMyEventsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMyEventsBinding.inflate(inflater, container, false)
        Toast.makeText(activity, "", 10).show()
        return binding.root
    }

    companion object {
        fun newInstance() = MyEventsFragment()
    }
}