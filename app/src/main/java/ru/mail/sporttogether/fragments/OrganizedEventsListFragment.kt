package ru.mail.sporttogether.fragments

import ru.mail.sporttogether.fragments.adapter.OrganizedEventsAdapter
import ru.mail.sporttogether.fragments.presenter.OrganizedEventsPresenterImpl
import ru.mail.sporttogether.fragments.view.OrganizedEventsListView
import ru.mail.sporttogether.mvp.PresenterActivity
import ru.mail.sporttogether.net.models.Event

/**
 * Created by bagrusss on 18.01.17
 */
class OrganizedEventsListFragment :
        AbstractEventsListFragment<OrganizedEventsPresenterImpl, OrganizedEventsAdapter>(),
        OrganizedEventsListView {

    override fun getPresenter() = OrganizedEventsPresenterImpl(this)

    override fun getAdapter(): OrganizedEventsAdapter {
        val act = activity as PresenterActivity<*>
        return OrganizedEventsAdapter(act.supportFragmentManager)
    }

    override fun resultEvent(e: Event) {
        listAdapter.resultEvent(e)
    }
}