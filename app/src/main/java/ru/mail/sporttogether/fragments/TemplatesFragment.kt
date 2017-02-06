package ru.mail.sporttogether.fragments

import ru.mail.sporttogether.fragments.adapter.TemplatesAdapter
import ru.mail.sporttogether.fragments.presenter.TemplatesPresenter
import ru.mail.sporttogether.fragments.presenter.TemplatesPresenterImpl
import ru.mail.sporttogether.fragments.view.TemplatesView

/**
 * Created by bagrusss on 05.02.17
 */
class TemplatesFragment : AbstractListFragment<TemplatesPresenter, TemplatesAdapter>(), TemplatesView {
    override fun getListPresenter() = TemplatesPresenterImpl(this)

    override fun getAdapter() = TemplatesAdapter()


}