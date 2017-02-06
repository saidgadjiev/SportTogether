package ru.mail.sporttogether.fragments

import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import ru.mail.sporttogether.R
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

    override fun getEmptyDrawable(): Drawable = ContextCompat.getDrawable(context, R.drawable.ic_organized)

    override fun getEmptyText(): String = getString(R.string.no_organized_events)

    override fun getListPresenter() = OrganizedEventsPresenterImpl(this)

    override fun getAdapter(): OrganizedEventsAdapter {
        val act = activity as PresenterActivity<*>
        return OrganizedEventsAdapter(act.supportFragmentManager)
    }

    override fun resultEvent(e: Event) {
        listAdapter.resultEvent(e)
    }
}