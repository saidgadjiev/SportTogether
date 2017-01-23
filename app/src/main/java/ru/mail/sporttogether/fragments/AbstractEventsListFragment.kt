package ru.mail.sporttogether.fragments

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import ru.mail.sporttogether.R
import ru.mail.sporttogether.databinding.FragmentEventsListBinding
import ru.mail.sporttogether.fragments.adapter.AbstractEventAdapter
import ru.mail.sporttogether.fragments.view.EventsListView
import ru.mail.sporttogether.fragments.presenter.AbstractEventsListPresenter
import ru.mail.sporttogether.mvp.PresenterFragment
import ru.mail.sporttogether.net.models.Event


/**
 * Created by bagrusss on 18.01.17
 */
abstract class AbstractEventsListFragment<PR : AbstractEventsListPresenter, T : AbstractEventAdapter<*>> :
        PresenterFragment<PR>(),
        EventsListView {

    private lateinit var binding: FragmentEventsListBinding
    protected lateinit var listAdapter: T
    private lateinit var eventsList: RecyclerView

    abstract fun getPresenter(): PR

    abstract fun getAdapter(): T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentEventsListBinding.inflate(inflater, container, false)

        eventsList = binding.eventsListRecyclerView
        eventsList.layoutManager = LinearLayoutManager(context)
        listAdapter = getAdapter()
        eventsList.adapter = listAdapter

        presenter = getPresenter()
        presenter.getEvents()
        return binding.root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.onCreate(savedInstanceState)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val item = menu.findItem(R.id.action_search)
        item.isVisible = false
    }


    override fun swapEvents(events: MutableList<Event>) {
        listAdapter.swap(events)
    }

    override fun deleteEvent(e: Event) {
        listAdapter.deleteEvent(e)
    }

}