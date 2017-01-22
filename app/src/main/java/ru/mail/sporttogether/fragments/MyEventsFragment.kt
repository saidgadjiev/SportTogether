package ru.mail.sporttogether.fragments

import ru.mail.sporttogether.fragments.adapter.MyEventsAdapter
import ru.mail.sporttogether.fragments.adapter.presenters.AbstractEventsListPresenter
import ru.mail.sporttogether.fragments.adapter.presenters.MyEventsPresenterImpl

class MyEventsFragment : AbstractEventsListFragment<AbstractEventsListPresenter, MyEventsAdapter>() {

    override fun getPresenter() = MyEventsPresenterImpl(this)

    override fun getAdapter() = MyEventsAdapter()

}