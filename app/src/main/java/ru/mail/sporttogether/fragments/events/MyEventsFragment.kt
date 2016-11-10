package ru.mail.sporttogether.fragments.events

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.mail.sporttogether.adapter.events.EventsAdapter
import ru.mail.sporttogether.databinding.FragmentMyEventsBinding
import ru.mail.sporttogether.fragments.PresenterFragment
import ru.mail.sporttogether.mvp.presenters.event.MyEventsPresenter
import ru.mail.sporttogether.mvp.presenters.event.MyEventsPresenterImpl
import ru.mail.sporttogether.mvp.views.event.IListEventView
import ru.mail.sporttogether.net.models.Event

class MyEventsFragment : PresenterFragment<MyEventsPresenter>(), IListEventView {

    override fun addEvent(event: Event) {

    }

    private lateinit var binding: FragmentMyEventsBinding
    private lateinit var eventsListView: RecyclerView
    private val adapter = EventsAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMyEventsBinding.inflate(inflater, container, false)

        eventsListView = binding.myEventsRecyclerView
        eventsListView.layoutManager = LinearLayoutManager(activity)
        eventsListView.adapter = adapter

        presenter = MyEventsPresenterImpl(this)
        presenter.getMyEvents()


        return binding.root
    }

    override fun loadEvents(events: MutableList<Event>) {
        adapter.swap(events)
    }

    companion object {
        fun newInstance() = MyEventsFragment()
    }
}