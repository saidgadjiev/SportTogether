package ru.mail.sporttogether.fragments.events.lists

import ru.mail.sporttogether.fragments.events.adapters.MyEventsAdapter
import ru.mail.sporttogether.mvp.presenters.event.lists.MyEventsPresenterImpl

class MyEventsFragment : AbstractEventsListFragment<MyEventsPresenterImpl, MyEventsAdapter>() {

    override fun getPresenter() = MyEventsPresenterImpl(this)

    override fun getAdapter() = MyEventsAdapter()

}