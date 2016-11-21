package ru.mail.sporttogether.fragments.events

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.mail.sporttogether.activities.AddEventActivity
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
    private lateinit var adapter: MyEventsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMyEventsBinding.inflate(inflater, container, false)
        presenter = MyEventsPresenterImpl(this)

        eventsListView = binding.myEventsRecyclerView
        eventsListView.layoutManager = LinearLayoutManager(activity)
        adapter = MyEventsAdapter(presenter)
        eventsListView.adapter = adapter

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

    override fun openEditActivity(id: Long) {
        AddEventActivity.startForResultEvent(activity, id)
    }

    companion object {
        fun newInstance() = MyEventsFragment()
    }
}