package ru.mail.sporttogether.fragments

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.mail.sporttogether.fragments.adapter.AbstractEventAdapter
import ru.mail.sporttogether.fragments.presenter.AbstractEventsListPresenter
import ru.mail.sporttogether.fragments.view.EventsListView
import ru.mail.sporttogether.net.models.Event


/**
 * Created by bagrusss on 18.01.17
 */
abstract class AbstractEventsListFragment<P : AbstractEventsListPresenter, A : AbstractEventAdapter<*>> :
        AbstractListFragment<P, A>(),
        EventsListView {


    abstract fun getEmptyDrawable(): Drawable
    abstract fun getEmptyText(): String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val v = super.onCreateView(inflater, container, savedInstanceState)
        data.emptyText.set(getEmptyText())
        data.emptyDrawable.set(getEmptyDrawable())
        presenter.getEvents()
        return v
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.onCreate(savedInstanceState)
    }


    override fun swapEvents(events: MutableList<Event>) {
        data.isEmpty.set(events.isEmpty())
        listAdapter.swap(events)
    }

    override fun deleteEvent(e: Event) {
        listAdapter.deleteEvent(e)
    }

    override fun updateEvent(event: Event) {
        listAdapter.updateEvent(event)
    }

}