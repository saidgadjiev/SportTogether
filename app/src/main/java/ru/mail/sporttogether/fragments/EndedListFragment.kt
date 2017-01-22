package ru.mail.sporttogether.fragments

import ru.mail.sporttogether.fragments.adapter.EndedEventsAdapter
import ru.mail.sporttogether.fragments.presenter.EndedEventsPresenterImpl
import ru.mail.sporttogether.fragments.presenter.AbstractEventsListPresenter

/**
 * Created by bagrusss on 08.10.16
 */
class EndedListFragment : AbstractEventsListFragment<AbstractEventsListPresenter, EndedEventsAdapter>() {

    override fun getPresenter() = EndedEventsPresenterImpl(this)

    override fun getAdapter() = EndedEventsAdapter()

}