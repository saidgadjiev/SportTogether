package ru.mail.sporttogether.fragments.events

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.mail.sporttogether.adapter.EventsAdapter
import ru.mail.sporttogether.databinding.FragmentEventsListBinding
import ru.mail.sporttogether.fragments.PresenterFragment
import ru.mail.sporttogether.mvp.presenters.event.EventsListPresenter
import ru.mail.sporttogether.mvp.presenters.event.EventsListPresenterImpl
import ru.mail.sporttogether.mvp.views.event.IListEventView
import ru.mail.sporttogether.net.models.Event

/**
 * Created by bagrusss on 08.10.16.
 *
 */
class EventsListFragment : PresenterFragment<EventsListPresenter>(), IListEventView {
    private lateinit var binding: FragmentEventsListBinding
    private lateinit var mEventsListView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.i("#MY " + javaClass.simpleName, "in on create view")
        binding = FragmentEventsListBinding.inflate(inflater, container, false)
        mEventsListView = binding.eventsListRecyclerView
        mEventsListView.layoutManager = LinearLayoutManager(activity)

        presenter = EventsListPresenterImpl(this)
        presenter.loadEvents()
        return binding.root
    }

    override fun loadEvents(events: List<Event>) {
        mEventsListView.adapter = EventsAdapter(events)
    }
}