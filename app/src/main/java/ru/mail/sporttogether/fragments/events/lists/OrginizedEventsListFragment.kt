package ru.mail.sporttogether.fragments.events.lists

import ru.mail.sporttogether.fragments.events.adapters.OrginizedEventsAdapter
import ru.mail.sporttogether.mvp.presenters.event.lists.AbstractEventsListPresenter
import ru.mail.sporttogether.mvp.presenters.event.lists.OrganizedEventsPresenterImpl

/**
 * Created by bagrusss on 18.01.17
 */
class OrginizedEventsListFragment : AbstractEventsListFragment<AbstractEventsListPresenter, OrginizedEventsAdapter>() {

    override fun getPresenter() = OrganizedEventsPresenterImpl(this)

    override fun getAdapter() = OrginizedEventsAdapter()
}