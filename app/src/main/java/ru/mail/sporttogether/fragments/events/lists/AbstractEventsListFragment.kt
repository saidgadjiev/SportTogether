package ru.mail.sporttogether.fragments.events.lists

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.mail.sporttogether.fragments.events.adapters.AbstractEventAdapter
import ru.mail.sporttogether.databinding.FragmentEventsListBinding
import ru.mail.sporttogether.fragments.PresenterFragment
import ru.mail.sporttogether.mvp.presenters.event.lists.EventsListPresenter
import ru.mail.sporttogether.mvp.views.event.IEventListView
import ru.mail.sporttogether.net.models.Event

/**
 * Created by bagrusss on 18.01.17
 */
abstract class AbstractEventsListFragment<PR : EventsListPresenter, out T : AbstractEventAdapter<*>> :
        PresenterFragment<PR>(),
        IEventListView {

    private lateinit var binding: FragmentEventsListBinding
    private lateinit var adapter: T
    private lateinit var eventsList: RecyclerView


    abstract fun getPresenter(): PR

    abstract fun getAdapter(): T

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentEventsListBinding.inflate(inflater, container, false)

        eventsList = binding.eventsListRecyclerView
        eventsList.layoutManager = LinearLayoutManager(context)
        adapter = getAdapter()
        eventsList.adapter = adapter

        presenter = getPresenter()
        presenter.getEvents()

        return binding.root
    }


    override fun swapEvents(events: MutableList<Event>) {
        adapter.swap(events)
    }

}