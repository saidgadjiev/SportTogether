package ru.mail.sporttogether.fragments

import android.os.Bundle
import android.view.View
import ru.mail.sporttogether.fragments.adapter.MyEventsAdapter
import ru.mail.sporttogether.fragments.presenter.MyEventsListPresenterImpl
import ru.mail.sporttogether.fragments.view.MyEventsListView
import ru.mail.sporttogether.net.models.Event

class MyEventsListFragment :
        AbstractEventsListFragment<MyEventsListPresenterImpl, MyEventsAdapter>(),
        MyEventsListView {

    override fun getPresenter() = MyEventsListPresenterImpl(this)

    override fun getAdapter() = MyEventsAdapter()

    override fun angryEvent(e: Event) {
        listAdapter.doAngry(e)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}