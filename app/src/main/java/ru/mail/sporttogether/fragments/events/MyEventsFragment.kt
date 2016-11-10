package ru.mail.sporttogether.fragments.events

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.mail.sporttogether.adapter.events.MyEventsAdapter
import ru.mail.sporttogether.databinding.FragmentMyEventsBinding
import ru.mail.sporttogether.fragments.PresenterFragment
import ru.mail.sporttogether.mvp.presenters.event.MyEventsPresenter
import ru.mail.sporttogether.mvp.presenters.event.MyEventsPresenterImpl
import ru.mail.sporttogether.mvp.views.event.IMyEventsView
import ru.mail.sporttogether.net.models.Event

class MyEventsFragment : PresenterFragment<MyEventsPresenter>(), IMyEventsView {

    private lateinit var binding: FragmentMyEventsBinding
    private lateinit var eventsListView: RecyclerView
    private val adapter = MyEventsAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMyEventsBinding.inflate(inflater, container, false)

        eventsListView = binding.myEventsRecyclerView
        eventsListView.layoutManager = LinearLayoutManager(activity)
        eventsListView.adapter = adapter

        presenter = MyEventsPresenterImpl(this)
        presenter.getMyEvents()

        return binding.root
    }

    override fun addOrganizedEvents(events: MutableList<Event>) {
        adapter.addOrganizedEvents(events)
    }

    override fun addEndedEvents(events: MutableList<Event>) {
        adapter.addEndedEvents(events)
    }

    override fun addMyEvents(events: MutableList<Event>) {
        adapter.addMyEvents(events)
    }

    override fun clearEvents() {
        adapter.clear()
    }

    companion object {
        fun newInstance() = MyEventsFragment()
    }
}