package ru.mail.sporttogether.fragments.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.mail.sporttogether.databinding.FragmentEventsListBinding
import ru.mail.sporttogether.fragments.PresenterFragment
import ru.mail.sporttogether.mvp.presenters.event.EventsListPresenter

/**
 * Created by bagrusss on 08.10.16.
 *
 */
class EventsListFragment : PresenterFragment<EventsListPresenter>() {
    private lateinit var binding: FragmentEventsListBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentEventsListBinding.inflate(inflater, container, false)
        return binding.root
    }
}