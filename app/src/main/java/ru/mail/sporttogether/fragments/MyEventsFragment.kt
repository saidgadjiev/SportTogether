package ru.mail.sporttogether.fragments

import ru.mail.sporttogether.fragments.adapter.MyEventsAdapter
import ru.mail.sporttogether.fragments.presenter.AbstractEventsListPresenter
import ru.mail.sporttogether.fragments.adapter.presenters.MyEventsHolderPresenterImpl

class MyEventsFragment : AbstractEventsListFragment<AbstractEventsListPresenter, MyEventsAdapter>() {

    override fun getPresenter() = MyEventsHolderPresenterImpl(this)

    override fun getAdapter() = MyEventsAdapter()

}