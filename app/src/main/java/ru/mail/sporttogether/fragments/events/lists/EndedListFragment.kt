package ru.mail.sporttogether.fragments.events.lists

import android.support.v7.widget.RecyclerView
import ru.mail.sporttogether.adapter.events.EventsAdapter
import ru.mail.sporttogether.databinding.FragmentEventsListBinding
import ru.mail.sporttogether.fragments.PresenterFragment
import ru.mail.sporttogether.mvp.presenters.event.lists.EventsListPresenter
import ru.mail.sporttogether.mvp.views.event.IListEventView
import ru.mail.sporttogether.net.models.Event

/**
 * Created by bagrusss on 08.10.16
 */
class EndedListFragment : PresenterFragment<EventsListPresenter>(), IListEventView {

    private lateinit var binding: FragmentEventsListBinding
    private lateinit var eventsListView: RecyclerView
    private val adapter = EventsAdapter()

    override fun loadEvents(events: MutableList<Event>) {
        adapter.swap(events)
    }

    override fun addEvent(event: Event) {
        adapter.addEvent(event)
    }
}