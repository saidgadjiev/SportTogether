package ru.mail.sporttogether.fragments.events.lists

import ru.mail.sporttogether.fragments.events.adapters.EndedEventsAdapter
import ru.mail.sporttogether.mvp.presenters.event.lists.EndedEventsPresenterImpl
import ru.mail.sporttogether.mvp.presenters.event.lists.AbstractEventsListPresenter

/**
 * Created by bagrusss on 08.10.16
 */
class EndedListFragment : AbstractEventsListFragment<AbstractEventsListPresenter, EndedEventsAdapter>() {

    override fun getPresenter() = EndedEventsPresenterImpl(this)

    override fun getAdapter() = EndedEventsAdapter()

}