package ru.mail.sporttogether.fragments

import ru.mail.sporttogether.fragments.adapter.EndedEventsAdapter
import ru.mail.sporttogether.fragments.presenter.EndedEventsPresenterImpl
import ru.mail.sporttogether.fragments.view.EndedEventsListView
import ru.mail.sporttogether.net.models.Event

/**
 * Created by bagrusss on 08.10.16
 */
class EndedListFragment :
        AbstractEventsListFragment<EndedEventsPresenterImpl, EndedEventsAdapter>(),
        EndedEventsListView {

    override fun getPresenter() = EndedEventsPresenterImpl(this)

    override fun getAdapter() = EndedEventsAdapter()

    override fun resultEvent(e: Event) {
        listAdapter.addResultedEvent(e)
    }


}