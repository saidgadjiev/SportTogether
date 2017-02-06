package ru.mail.sporttogether.fragments

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import ru.mail.sporttogether.R
import ru.mail.sporttogether.fragments.adapter.MyEventsAdapter
import ru.mail.sporttogether.fragments.presenter.MyEventsListPresenterImpl
import ru.mail.sporttogether.fragments.view.MyEventsListView
import ru.mail.sporttogether.net.models.Event

class MyEventsListFragment :
        AbstractEventsListFragment<MyEventsListPresenterImpl, MyEventsAdapter>(),
        MyEventsListView {
    override fun getEmptyDrawable(): Drawable = ContextCompat.getDrawable(context, R.drawable.ic_my_location)

    override fun getEmptyText(): String = getString(R.string.no_my_events)

    override fun getListPresenter() = MyEventsListPresenterImpl(this)

    override fun getAdapter() = MyEventsAdapter()

    override fun angryEvent(e: Event) {
        listAdapter.doAngry(e)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}