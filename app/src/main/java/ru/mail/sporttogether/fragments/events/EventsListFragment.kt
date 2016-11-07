package ru.mail.sporttogether.fragments.events

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
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
    private lateinit var eventsListView: RecyclerView
    private val adapter = EventsAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentEventsListBinding.inflate(inflater, container, false)
        eventsListView = binding.eventsListRecyclerView
        eventsListView.layoutManager = LinearLayoutManager(context)

        presenter = EventsListPresenterImpl(this)
        presenter.loadEvents()
        eventsListView.adapter = adapter
        return binding.root
    }

    override fun loadEvents(events: MutableList<Event>) {
        adapter.swap(events)
    }

    override fun addEvent(event: Event) {
        adapter.addEvent(event)
    }
}