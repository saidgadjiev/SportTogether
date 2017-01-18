package ru.mail.sporttogether.fragments.events.lists

import ru.mail.sporttogether.fragments.events.adapters.OrginizedEventsAdapter
import ru.mail.sporttogether.mvp.presenters.event.lists.EventsListPresenter
import ru.mail.sporttogether.mvp.presenters.event.lists.OrginizedEventsPresenterImpl

/**
 * Created by bagrusss on 18.01.17
 */
class OrginizedEventsListFragment : AbstractEventsListFragment<EventsListPresenter, OrginizedEventsAdapter>() {

    override fun getPresenter() = OrginizedEventsPresenterImpl(this)

    override fun getAdapter() = OrginizedEventsAdapter()
}