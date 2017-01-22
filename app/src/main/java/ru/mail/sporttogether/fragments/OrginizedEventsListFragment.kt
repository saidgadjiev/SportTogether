package ru.mail.sporttogether.fragments

import ru.mail.sporttogether.fragments.adapter.OrginizedEventsAdapter
import ru.mail.sporttogether.fragments.adapter.presenters.AbstractEventsListPresenter
import ru.mail.sporttogether.fragments.adapter.presenters.OrganizedEventsPresenterImpl

/**
 * Created by bagrusss on 18.01.17
 */
class OrginizedEventsListFragment : AbstractEventsListFragment<AbstractEventsListPresenter, OrginizedEventsAdapter>() {

    override fun getPresenter() = OrganizedEventsPresenterImpl(this)

    override fun getAdapter() = OrginizedEventsAdapter()
}