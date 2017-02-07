package ru.mail.sporttogether.fragments

import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import ru.mail.sporttogether.R
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

    override fun getEmptyDrawable(): Drawable = ContextCompat.getDrawable(context, R.mipmap.ic_launcher)

    override fun getEmptyText(): String = getString(R.string.no_ended_events)

    override fun getListPresenter() = EndedEventsPresenterImpl(this)

    override fun getAdapter() = EndedEventsAdapter()

    override fun resultEvent(e: Event) {
        listAdapter.addResultedEvent(e)
    }


}